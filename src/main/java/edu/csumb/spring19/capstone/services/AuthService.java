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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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


    public RestDTO signin(String username, String password) {
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

    public RestDTO resetPassword(String username) throws Exception {
        Optional<PLUser> user = userRepository.findById(username);
        if (!user.isPresent()) throw new Exception("User does not exist.");
        user.get().resetPassword();
        userRepository.save(user.get());
        return new RestSuccess();
    }
}
