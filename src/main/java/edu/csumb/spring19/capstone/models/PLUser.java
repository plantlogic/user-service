package edu.csumb.spring19.capstone.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Document(collection = "users")
public class PLUser {
    @Id
    private String username;
    private String password;
    private String email;
    private String realName;
    private Calendar passwordUpdated;
    private Boolean passwordReset;
    private List<String> ranchAccess;
    private List<GrantedAuthority> permissions;
    private String shipperID;

    public PLUser(String username, String password, String realName, String email, List<String> ranchAccess,
                  List<GrantedAuthority> permissions, Boolean passwordReset, String shipperID){
        this.username = username;
        this.password = password;
        this.realName = realName;
        this.email = email;
        this.passwordUpdated = Calendar.getInstance();
        this.ranchAccess = ranchAccess;
        this.permissions = permissions;
        this.passwordReset = passwordReset;
        this.shipperID = shipperID;
    }


    // ===================
    // ACCESSORS
    // ===================
    public String getEmail() { return this.email; }
    
    @JsonIgnore
    public List<GrantedAuthority> getNonNullPermissions() { return this.permissions; }
    
    @JsonIgnore
    public String getPassword(){ return this.password;}
    
    public Calendar getPasswordUpdated() { return this.passwordUpdated; }
    
    public List<GrantedAuthority> getPermissions() {
        return (passwordReset) ? new ArrayList<>() : this.permissions;
    }
    
    public List<String> getRanchAccess() { return ranchAccess; }
    
    public String getRealName() { return this.realName; }
    
    public String getShipperID() {
        return (Strings.isNullOrEmpty(this.shipperID)) ? "" : this.shipperID;
    }
    
    public String getUsername() { return this.username; }
    
    // ===================
    // MUTATORS
    // ===================
    public void setRanchAccess(List<String> ranchAccess) { this.ranchAccess = ranchAccess; }
    
    public void setShipperID(String shipperID) { this.shipperID = shipperID; }  
    

    // ===================
    // BOOLEAN CHECKS
    // ===================
    public Boolean hasEmail() { return !Strings.isNullOrEmpty(this.email); }

    public Boolean isPasswordReset() { return passwordReset; }


    // ===================
    // Quick Edit Methods
    // ===================
    public void resetPassword() { this.passwordReset = true; }

    public void changePassword(String newPassword) {
        this.password = newPassword;
        this.passwordReset = false;
        this.passwordUpdated = Calendar.getInstance();
    }

    public void importEdits(String username, String email, String realName, List<String> ranchAccess, List<GrantedAuthority> permissions, String shipperID) {
        this.username = username;
        this.email = email;
        this.realName = realName;
        this.ranchAccess = ranchAccess;
        this.permissions = permissions;
        this.shipperID = shipperID;
    }
}
