package edu.csumb.spring19.capstone.services;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailService mailService;

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
     * Deletes user with the specified username
     * @param username The username to look for
     * @return UserInfoSend type with success/failure
     */
    public RestDTO deleteUser(String username) {
        Optional<PLUser> user = userRepository.findByUsernameIgnoreCase(username);
        if (getCurrentUsername().equals(username)) {
            return new RestFailure("You can't delete the current user.");
        } else if (user.isPresent()) {
            userRepository.deleteByUsername(username);
            return new RestSuccess();
        } else {
            return new RestFailure("No user found with that username.");
        }
    }

    public RestDTO editUser(UserInfoReceiveEdit editedUser) {
        if (editedUser.usernameChanged() && userRepository.existsByUsername(editedUser.getUsername())) {
            return new RestFailure("That username already exists.");
        }

        List<GrantedAuthority> parsedPermissions = parsePermissions(editedUser.getPermissions());
        if (!parsedPermissions.contains(PLRole.USER_MANAGEMENT)) {
            return new RestFailure("Admins with 'User Management' permission " +
                  "cannot remove their own 'User Management' permissions.");
        }

        Optional<PLUser> user = userRepository.findByUsernameIgnoreCase(editedUser.getInitialUsername());
        if (user.isPresent()) {
            user.get().importEdits(
                  editedUser.getUsername(),
                  editedUser.getEmail(),
                  editedUser.getRealName(),
                  parsedPermissions
                  );
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
        if (user.anyEmptyVal()) return new RestFailure("All fields must be filled.");
        if (userRepository.existsByUsername(user.getUsername())) return new RestFailure("That user already exists. Please change username.");

        String pass = PasswordGenerator.newPass();
        userRepository.save(new PLUser(user.getUsername(), passwordEncoder.encode(pass), user.getRealName(), user.getEmail(),
              parsePermissions(user.getPermissions()), true));
        mailService.newAccountCreated(user.getEmail(), user.getRealName(), user.getUsername(), pass);
        return new RestSuccess();
    }

    /**
     * Generates a random password for the user and emails it to them, forcing them to change their password on next log in
     */
    @SuppressWarnings("Duplicates")
    public RestDTO resetPassword(String username) {
        Optional<PLUser> user = userRepository.findByUsernameIgnoreCase(username);
        if (user.isPresent()) {
            String pass = PasswordGenerator.newPass();
            user.get().changePassword(passwordEncoder.encode(pass));
            user.get().resetPassword();
            userRepository.save(user.get());
            mailService.passwordReset(user.get().getEmail(), user.get().getRealName(), pass);
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
                        new ArrayList<GrantedAuthority>(Arrays.asList(
                              PLRole.DATA_VIEW, PLRole.DATA_EDIT, PLRole.DATA_ENTRY,
                              PLRole.USER_MANAGEMENT, PLRole.APP_ADMIN
                        )),
                        false
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
