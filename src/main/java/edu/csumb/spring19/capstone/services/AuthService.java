package edu.csumb.spring19.capstone.services;

import edu.csumb.spring19.capstone.dto.RestDTO;
import edu.csumb.spring19.capstone.dto.RestData;
import edu.csumb.spring19.capstone.dto.RestFailure;
import edu.csumb.spring19.capstone.dto.auth.AuthDTO;
import edu.csumb.spring19.capstone.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userRepository;

    /**
     * Authenticate a user with the specified username and password
     * @param username
     * @param password
     * @return
     */
    public RestDTO signin(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return new RestData<>(
                  new AuthDTO(
                        jwtTokenProvider.createToken(
                              username,
                              userRepository.loadUserByUsername(username).getAuthorities()
                        ),
                        userRepository.getUserDTO(username).get()
                  )
            );
        } catch (Exception e) {
            //return new RestFailure("Username or password was incorrect.");
            return new RestFailure(e.getMessage());
        }
    }
}
