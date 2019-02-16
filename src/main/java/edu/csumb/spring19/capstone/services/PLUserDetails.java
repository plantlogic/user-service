package edu.csumb.spring19.capstone.services;

import com.mongodb.operation.UserExistsOperation;
import edu.csumb.spring19.capstone.models.PLUser;
import edu.csumb.spring19.capstone.repositories.UserRepository;
import edu.csumb.spring19.capstone.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PLUserDetails implements UserDetailsService {
    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<PLUser> user = repository.findById(username);
        if(!user.isPresent()) {
            throw new UsernameNotFoundException("User not found.");
        }

        return new User(user.get().getUsername(), user.get().getPassword(), user.get().getPermissions());
    }

    public Boolean containsUsername(String username) {
        return repository.findById(username).isPresent();
    }

    public Long size() {
        return repository.count();
    }

    public Boolean addUser(String username, String password, String realName, String email) throws Exception {
        if (containsUsername(username)) throw new Exception("User already exists.");
        return repository.save(new PLUser(username, password, realName, email)) != null;
    }
}
