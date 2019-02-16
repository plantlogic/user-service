package edu.csumb.spring19.capstone.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
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
    private ArrayList<SimpleGrantedAuthority> permissions = new ArrayList<>();

    public PLUser(String username, String password, String realName, String email){
        this.username = username;
        this.password = password;
        this.realName = realName;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void changePassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return this.password;
    }

    public List<SimpleGrantedAuthority> getPermissions() {
        return this.permissions;
    }
}