package edu.csumb.spring19.capstone.services;

import edu.csumb.spring19.capstone.dto.RestDTO;
import edu.csumb.spring19.capstone.dto.RestData;
import edu.csumb.spring19.capstone.dto.RestFailure;
import edu.csumb.spring19.capstone.dto.RestSuccess;
import edu.csumb.spring19.capstone.dto.auth.AuthDTO;
import edu.csumb.spring19.capstone.helpers.PasswordGenerator;
import edu.csumb.spring19.capstone.models.PLUser;
import edu.csumb.spring19.capstone.repositories.UserRepository;
import edu.csumb.spring19.capstone.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public RestDTO signIn(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return new RestData<>(
                  new AuthDTO(
                        jwtTokenProvider.createToken(
                              username,
                              userService.loadUserByUsername(username).getAuthorities()
                        ),
                        userService.getUserDTO(username).get()
                  )
            );
        } catch (Exception e) {
            return new RestFailure("Username or password is incorrect.");
        }
    }

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
}
