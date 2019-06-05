package edu.csumb.spring19.capstone.dto.user;

import edu.csumb.spring19.capstone.models.PLUser;
import org.springframework.security.core.GrantedAuthority;

import java.util.Calendar;
import java.util.List;

public class UserInfoSend extends UserDTO {
    private String realName;
    private String email;
    private Calendar passwordUpdated;
    private List<String> ranchAccess;
    private List<GrantedAuthority> permissions;
    private Boolean passwordReset;

    public UserInfoSend(PLUser user) {
        super.username = user.getUsername();
        this.realName = user.getRealName();
        this.email = user.getEmail();
        this.passwordUpdated = user.getPasswordUpdated();
        this.ranchAccess = user.getRanchAccess();
        this.permissions = user.getPermissions();
        this.passwordReset = user.isPasswordReset();
    }

    public UserInfoSend(String username, String realName, String email, Calendar passwordUpdated,
                        List<String> ranchAccess, List<GrantedAuthority> permissions, Boolean passwordReset) {
        super.username = username;
        this.realName = realName;
        this.email = email;
        this.passwordUpdated = passwordUpdated;
        this.ranchAccess = ranchAccess;
        this.permissions = permissions;
        this.passwordReset = passwordReset;
    }

    public String getUsername() {
        return super.username;
    }

    public String getRealName() {
        return realName;
    }

    public String getEmail() {
        return email;
    }

    public Calendar getPasswordUpdated() {
        return passwordUpdated;
    }

    public List<String> getRanchAccess() {
        return ranchAccess;
    }

    public List<GrantedAuthority> getPermissions() {
        return permissions;
    }

    public Boolean getPasswordReset() {
        return passwordReset;
    }
}
