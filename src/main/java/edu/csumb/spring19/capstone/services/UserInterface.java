package edu.csumb.spring19.capstone.services;

import javax.servlet.http.HttpServletRequest;

import edu.csumb.spring19.capstone.dto.RestDTO;
import edu.csumb.spring19.capstone.dto.RestData;
import edu.csumb.spring19.capstone.dto.RestFailure;
import edu.csumb.spring19.capstone.models.PLUser;
import edu.csumb.spring19.capstone.repositories.UserRepository;
import edu.csumb.spring19.capstone.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserInterface {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    public RestDTO signin(String username, String password) throws Throwable {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return new RestData<>(jwtTokenProvider.createToken(username, userRepository.findById(username).get().getPermissions()));
        } catch (AuthenticationException e) {
            return new RestFailure("Username or password was incorrect.");
        }
    }

    public Iterable<PLUser> allUsers() {
        return userRepository.findAll();
    }
}
