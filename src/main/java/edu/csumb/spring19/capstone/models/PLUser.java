package edu.csumb.spring19.capstone.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

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
    private Boolean passwordReset;
    private List<GrantedAuthority> permissions;

    public PLUser(String username, String password, String realName, String email, List<GrantedAuthority> permissions,
                  Boolean passwordReset){
        this.username = username;
        this.password = password;
        this.realName = realName;
        this.email = email;
        this.passwordUpdated = Calendar.getInstance();
        this.permissions = permissions;
        this.passwordReset = passwordReset;
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
        if (passwordReset) return new ArrayList<>();
        else return this.permissions;
    }

    public Boolean isPasswordReset() {
        return passwordReset;
    }


    // ===================
    // Quick Edit Methods
    // ===================

    public void resetPassword() {
        this.passwordReset = true;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
        this.passwordReset = false;
        this.passwordUpdated = Calendar.getInstance();
    }

    public void importEdits(String username, String email, String realName, List<GrantedAuthority> permissions) {
        this.username = username;
        this.email = email;
        this.realName = realName;
        this.permissions = permissions;
    }
}