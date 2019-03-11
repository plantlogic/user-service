package edu.csumb.spring19.capstone.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Document(collection = "users")
public class PLUser {
    @Id
    @Size(min = 4, message = "Usernames must be at least 4 characters.")
    private String username;
    @Size(min = 8, message = "Passwords must be at least 8 characters.")
    private String password;
    @Email(message = "Valid email addresses are required.")
    private String email;
    @NotEmpty
    private String realName;
    private Calendar passwordUpdated;
    private ArrayList<GrantedAuthority> permissions;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public PLUser(String username, String password, String realName, String email, ArrayList<GrantedAuthority> permissions){
        this.username = username;
        this.password = password;
        this.realName = realName;
        this.email = email;
        this.passwordUpdated = Calendar.getInstance();
        this.permissions = permissions;
    }

    public String getUsername() {
        return this.username;
    }

    @JsonIgnore
    public String getPassword(){
        return this.password;
    }

    public String getEmail() {
        return this.email;
    }

    public String getRealName() {
        return this.realName;
    }

    public Calendar getPasswordUpdated() {
        return this.passwordUpdated;
    }

    public List<GrantedAuthority> getPermissions() {
        return this.permissions;
    }


    public void changePassword(String oldPassword, String newPassword) throws Exception {
        if (passwordEncoder.matches(oldPassword, this.password)) {
            this.password = newPassword;
            this.passwordUpdated = Calendar.getInstance();
        } else throw new Exception("Password incorrect");
    }
}