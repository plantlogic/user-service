package edu.csumb.spring19.capstone.services;

import edu.csumb.spring19.capstone.dto.RestDTO;
import edu.csumb.spring19.capstone.dto.RestData;
import edu.csumb.spring19.capstone.dto.RestFailure;
import edu.csumb.spring19.capstone.dto.RestSuccess;
import edu.csumb.spring19.capstone.dto.auth.AuthDTO;
import edu.csumb.spring19.capstone.models.PLUser;
import edu.csumb.spring19.capstone.repositories.UserRepository;
import edu.csumb.spring19.capstone.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SelfService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserService userService;


    public RestDTO changePassword(String oldPassword, String newPassword) throws Exception {
        PLUser user = getCurrentUser();
        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            if (passwordEncoder.matches(newPassword, user.getPassword())) {
                return new RestFailure("Your new password must be different from your current password.");
            } else {
                user.changePassword(passwordEncoder.encode(newPassword));
                saveCurrentUser(user);
                return new RestSuccess();
            }
        } else return new RestFailure("Current password is incorrect.");
    }

    public RestDTO renewToken() {
        String username = getCurrentUsername();
        return new RestData<>(
              new AuthDTO(
                    jwtTokenProvider.createToken(
                          username,
                          userService.loadUserByUsername(username).getAuthorities()
                    ),
                    userService.getUserDTO(username).get()
              )
        );
    }




    // ==============
    // Internal code
    // ==============

    private PLUser getCurrentUser() throws Exception {
        Optional<PLUser> user = userRepository.findByUsernameIgnoreCase(getCurrentUsername());
        if (!user.isPresent()) throw new Exception("User does not exist.");
        return user.get();
    }

    private Boolean saveCurrentUser(PLUser adjusted) throws Exception {
        if (!getCurrentUsername().equals(adjusted.getUsername()) && userRepository.existsByUsername(adjusted.getUsername()))
            throw new Exception("You're trying to overwrite a different user, which is not allowed.");

        userRepository.save(adjusted);
        return true;
    }

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
