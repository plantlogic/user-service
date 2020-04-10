package edu.csumb.spring19.capstone.services;

import com.google.common.base.Strings;
import edu.csumb.spring19.capstone.dto.RestDTO;
import edu.csumb.spring19.capstone.dto.RestData;
import edu.csumb.spring19.capstone.dto.RestFailure;
import edu.csumb.spring19.capstone.dto.RestSuccess;
import edu.csumb.spring19.capstone.dto.user.UserInfoReceive;
import edu.csumb.spring19.capstone.dto.user.UserInfoReceiveEdit;
import edu.csumb.spring19.capstone.dto.user.UserInfoSend;
import edu.csumb.spring19.capstone.helpers.PasswordGenerator;
import edu.csumb.spring19.capstone.models.PLRole;
import edu.csumb.spring19.capstone.models.PLUser;
import edu.csumb.spring19.capstone.repositories.UserRepository;
import org.apache.commons.text.WordUtils;
// import org.apache.commons.text.similarity.EditDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailService mailService;

    private final Pattern usernameRegex = Pattern.compile("^[a-zA-Z0-9._-]+$");
    private final String usernameRegexError = "Usernames may only contain alphanumeric characters and periods, underscores, or dashes.";
    private final Pattern realNameRegex = Pattern.compile("^[a-zA-Z]+([ -][a-zA-Z]+)+$");
    private final String realNameRegexError = "Please enter the employee's full name like this: \"Jane Doe\". No special characters other" +
          " than spaces and dashes are allowed.";


    /**
     * Gets data on all users from mongoDB
     * @return Returns the data of all users
     */
    public Iterable<PLUser> allUsers() {
        return userRepository.findAll();
    }

    /**
     * Gets data for a user with the specified username
     * @param username The username to look for
     * @return UserInfoSend type with name/username/email/permissions
     */
    public RestDTO getUser(String username) {
        Optional<UserInfoSend> user = this.getUserDTO(username);
        if (user.isPresent()) {
            return new RestData<>(user);
        } else {
            return new RestFailure("No user found with that username.");
        }
    }

    /**
     * When a user has a required password reset, their permissions are
     * temporarily null until fixed - this method gets those permissions anyway
     */
    public RestDTO getUserWithoutNulledPerms(String username) {
        Optional<PLUser> user = userRepository.findByUsernameIgnoreCase(username);
        if (user.isPresent()) {
            return new RestData<>(new UserInfoSend(
                  user.get().getUsername(),
                  user.get().getRealName(),
                  user.get().getEmail(),
                  user.get().getPasswordUpdated(),
                  user.get().getRanchAccess(),
                  user.get().getNonNullPermissions(),
                  user.get().isPasswordReset(),
                  user.get().getShipperID()));
        } else {
            return new RestFailure("No user found with that username.");
        }
    }

    /**
     * Deletes user with the specified username
     * @param username The username to look for
     * @return UserInfoSend type with success/failure
     */
    public RestDTO deleteUser(String username) {
        Optional<PLUser> user = userRepository.findByUsernameIgnoreCase(username);
        if (getCurrentUsername().equals(username)) {
            return new RestFailure("You can't delete the current user.");
        } else if (user.isPresent()) {
            userRepository.deleteByUsernameIgnoreCase(username);
            return new RestSuccess();
        } else {
            return new RestFailure("No user found with that username.");
        }
    }

    /**
     * Overwrites values in a user's info
     */
    public RestDTO editUser(UserInfoReceiveEdit editedUser) {
        if (editedUser.anyEmptyVal()) return new RestFailure("All fields must be filled.");
        if (editedUser.hasBothPassAndEmail()) return new RestFailure("Users can only have either an email or a manually entered password.");
        if (editedUser.getUsername().length() > 0 && editedUser.getUsername().length() < 4)
            return new RestFailure("Usernames must be at least 4 characters long.");
        if (!Strings.isNullOrEmpty(editedUser.getPassword()) && editedUser.getPassword().length() > 0 && editedUser.getPassword().length() < 4)
            return new RestFailure("Passwords must be at least 4 characters long.");

        editedUser.unifyStringCase();

        if (editedUser.usernameChanged() && userRepository.existsByUsernameIgnoreCase(editedUser.getUsername())) {
            return new RestFailure("That username already exists.");
        }

        List<GrantedAuthority> parsedPermissions = parsePermissions(editedUser.getPermissions());
        if (editedUser.getInitialUsername().equalsIgnoreCase(getCurrentUsername()) && !parsedPermissions.contains(PLRole.USER_MANAGEMENT)) {
            return new RestFailure("Admins with 'User Management' permission " +
                  "cannot remove their own 'User Management' permissions.");
        }

        // Is this needed? -> Error when non-email user tries to use user management privilege
        // if (editedUser.getInitialUsername().equalsIgnoreCase(getCurrentUsername()) && Strings.isNullOrEmpty(editedUser.getEmail())) {
        //     return new RestFailure("Admins with 'User Management' permission " +
        //           "cannot remove their own email address.");
        // }

        if (!usernameRegex.matcher(editedUser.getUsername()).matches()) {
            return new RestFailure(usernameRegexError);
        }

        if (!realNameRegex.matcher(editedUser.getRealName()).matches()) {
            return new RestFailure(realNameRegexError);
        }

        Optional<PLUser> user = userRepository.findByUsernameIgnoreCase(editedUser.getInitialUsername());
        if (user.isPresent()) {
            boolean hadEmail = user.get().hasEmail(); // false

            user.get().importEdits(
                  editedUser.getUsername().toLowerCase(),
                  editedUser.getEmail(),
                  WordUtils.capitalizeFully(editedUser.getRealName(), ' ', '-'),
                  editedUser.getRanchAccess(),
                  parsedPermissions,
                  editedUser.getShipperID()
                  );
            // If user doesn't have an email address and password is provided, import the new password
            if (!user.get().hasEmail() && !Strings.isNullOrEmpty(editedUser.getPassword()))
                user.get().changePassword(passwordEncoder.encode(editedUser.getPassword()));
            // Otherwise if the user has an email and didn't have an email, send them a new password
            else if (user.get().hasEmail() && !hadEmail) {
                String pass = PasswordGenerator.newPass();
                user.get().changePassword(passwordEncoder.encode(pass));
                user.get().resetPassword();

                try {
                    mailService.passwordReset(user.get().getEmail(), user.get().getRealName(), pass);
                } catch (MessagingException e) {
                    return new RestFailure("There was an error sending the password in an email. Please try again.");
                }
            }

            // Delete old user entry from DB if username has been changed
            if (editedUser.usernameChanged()) userRepository.deleteByUsernameIgnoreCase(editedUser.getInitialUsername());
            userRepository.save(user.get());
            return new RestSuccess();
        } else {
            return new RestFailure("No user found with that username.");
        }
    }

    /**
     * Adds a user to the database.
     * @param user The user to add to the database. Password will be randomly generated.
     * @return RestDTO with success or failure and associated message.
     */
    public RestDTO addUser(UserInfoReceive user) {
        System.out.println("UserService received UserInfoReceive With Shipper ID: ["+user.getShipperID()+"] ");
        if (user.anyEmptyVal()) return new RestFailure("All fields must be filled.");
        if (user.hasBothPassAndEmail()) return new RestFailure("Users can only have either an email or a manually entered password.");
        if (user.hasNeitherPassOrEmail()) return new RestFailure("Users must have either an email or a manually entered password.");
        if (user.getUsername().length() > 0 && user.getUsername().length() < 4)
            return new RestFailure("Usernames must be at least 4 characters long.");
        if (!Strings.isNullOrEmpty(user.getPassword()) && user.getPassword().length() > 0 && user.getPassword().length() < 4)
            return new RestFailure("Passwords must be at least 4 characters long.");
        user.unifyStringCase();

        if (userRepository.existsByUsernameIgnoreCase(user.getUsername())) return new RestFailure("That user already exists. Please change username.");

        if (!usernameRegex.matcher(user.getUsername()).matches()) {
            return new RestFailure(usernameRegexError);
        }

        if (!realNameRegex.matcher(user.getRealName()).matches()) {
            return new RestFailure(realNameRegexError);
        }


        String pass = PasswordGenerator.newPass();
        PLUser plUser = new PLUser(
              user.getUsername().toLowerCase(),
              passwordEncoder.encode(pass),
              WordUtils.capitalizeFully(user.getRealName(), ' ', '-'),
              user.getEmail(),
              user.getRanchAccess(),
              parsePermissions(user.getPermissions()),
              true,
              user.getShipperID()
        );

        System.out.println("UserService Created PLUser With Shipper ID: ["+plUser.getShipperID()+"] ");

        // If user doesn't have an email address, import manually entered password
        if (!plUser.hasEmail()) plUser.changePassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(plUser);

        if (plUser.hasEmail()) {
            try {
                mailService.newAccountCreated(plUser.getEmail(), plUser.getRealName(), plUser.getUsername(), pass);
            } catch (MessagingException e) {
                userRepository.delete(plUser);
                return new RestFailure("There was an error sending the password email. Please try again.");
            }
        }
        return new RestSuccess();
    }

    /**
     * Generates a random password for the user and emails it to them, forcing them to change their password on next log in
     */
    @SuppressWarnings("Duplicates")
    public RestDTO resetPassword(String username) {
        Optional<PLUser> user = userRepository.findByUsernameIgnoreCase(username);
        if (user.isPresent()) {
            if (!user.get().hasEmail()) return new RestFailure("User has a manually entered password.");
            String pass = PasswordGenerator.newPass();
            user.get().changePassword(passwordEncoder.encode(pass));
            user.get().resetPassword();
            userRepository.save(user.get());
            try {
                mailService.passwordReset(user.get().getEmail(), user.get().getRealName(), pass);
            } catch (MessagingException e) {
                return new RestFailure("There was an error sending the password reset email. Please try again.");
            }
            return new RestSuccess();
        } else {
            return new RestFailure("No user found with that username.");
        }
    }

    // ===============
    // Special-use code
    // ===============

    /**
     * Used to add a default user to the install if no users exist
     */
    public void addDefaultUser() {
        if (this.userCount() < 1) {
            userRepository.save(
                  new PLUser(
                        "admin",
                        passwordEncoder.encode("admin"),
                        "Default Admin",
                        "hello@plantlogic.org",
                        new ArrayList<>(),
                        new ArrayList<GrantedAuthority>(Arrays.asList(
                              PLRole.DATA_VIEW, PLRole.DATA_EDIT, PLRole.DATA_ENTRY,
                              PLRole.USER_MANAGEMENT, PLRole.APP_ADMIN, PLRole.CONTRACTOR_VIEW, PLRole.CONTRACTOR_EDIT
                        )),
                        false,
                        ""
                  )
            );
        }
    }

    /**
     * Converts a list of string permissions into a list of granted authorities using our PLRole
     * @param in List of strings matching PLRole enum
     * @return List of granted authorities implemented by PLRole enum
     */
    private List<GrantedAuthority> parsePermissions(List<String> in) {
        return in.stream().map(PLRole::valueOf).collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<PLUser> user = userRepository.findByUsernameIgnoreCase(username);
        if(!user.isPresent()) {
            throw new UsernameNotFoundException("User not found.");
        }

        return new User(user.get().getUsername(), user.get().getPassword(), user.get().getPermissions());
    }

    public Optional<UserInfoSend> getUserDTO(String username){
        Optional<PLUser> user = userRepository.findByUsernameIgnoreCase(username);
        return user.map(UserInfoSend::new);
    }

    /**
     * User count
     * @return The number of users in the database
     */
    public long userCount() {
        return userRepository.count();
    }


    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
