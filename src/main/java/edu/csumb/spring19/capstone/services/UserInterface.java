package edu.csumb.spring19.capstone.services;

import edu.csumb.spring19.capstone.dto.RestDTO;
import edu.csumb.spring19.capstone.dto.RestData;
import edu.csumb.spring19.capstone.dto.RestFailure;
import edu.csumb.spring19.capstone.dto.RestSuccess;
import edu.csumb.spring19.capstone.dto.user.UserInfoReceive;
import edu.csumb.spring19.capstone.dto.user.UserInfoSend;
import edu.csumb.spring19.capstone.models.PLUser;
import edu.csumb.spring19.capstone.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInterface implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    public Optional<UserInfoSend> getUserDTO(String username){
        Optional<PLUser> user = userRepository.findById(username);
        if (user.isPresent()) {
            return Optional.of(new UserInfoSend(user.get()));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Deletes user with the specified username
     * @param username The username to look for
     * @return UserInfoSend type with success/failure
     */
    public RestDTO deleteUser(String username) {
        Optional<PLUser> user = userRepository.findById(username);
        if (user.isPresent()) {
            try {
                userRepository.deleteById(username);
            } catch (Exception e) {
                return new RestFailure(e.getMessage());
            }

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
        if (userRepository.existsById(user.getUsername())) return new RestFailure("User already exists.");

        try {
            String pass = generatePassword();
            userRepository.save(new PLUser(user.getUsername(), passwordEncoder.encode(pass), user.getRealName(), user.getEmail()));
            return new RestData<>(pass);
        } catch (Exception e) {
            return new RestFailure(e.getMessage());
        }
    }


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
                        "hello@plantlogic.org"
                  )
            );
        }
    }

    /**
     * Generates a 20-character alphanumeric string for a password - will be trashed when password reset
     * links are implemented
     * @return 20 character alphanumeric string for random password
     */
    // TODO: Temporary alternative to emailing a password reset link
    private String generatePassword() {
        char[] alph = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        String pass = "";
        for (int i = 0; i < 20; i++) {
            pass = pass + String.valueOf(alph[(new Long(Math.round(Math.random()*alph.length))).intValue()]);
        }
        return pass;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<PLUser> user = userRepository.findById(username);
        if(!user.isPresent()) {
            throw new UsernameNotFoundException("User not found.");
        }

        return new User(user.get().getUsername(), user.get().getPassword(), user.get().getPermissions());
    }

    /**
     * User count
     * @return The number of users in the database
     */
    public long userCount() {
        return userRepository.count();
    }
}
