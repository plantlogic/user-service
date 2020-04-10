package edu.csumb.spring19.capstone.dto.user;

import edu.csumb.spring19.capstone.models.PLUser;
import org.springframework.security.core.GrantedAuthority;

import java.util.Calendar;
import java.util.List;

import com.google.common.base.Strings;

public class UserInfoSend extends UserDTO {
    private String realName;
    private String email;
    private Calendar passwordUpdated;
    private List<String> ranchAccess;
    private List<GrantedAuthority> permissions;
    private Boolean passwordReset;
    private String shipperID;

    public UserInfoSend(PLUser user) {
        super.username = user.getUsername();
        this.realName = user.getRealName();
        this.email = user.getEmail();
        this.passwordUpdated = user.getPasswordUpdated();
        this.ranchAccess = user.getRanchAccess();
        this.permissions = user.getPermissions();
        this.passwordReset = user.isPasswordReset();
        this.shipperID = user.getShipperID();
    }

    public UserInfoSend(String username, String realName, String email, Calendar passwordUpdated,
                        List<String> ranchAccess, List<GrantedAuthority> permissions, Boolean passwordReset, String shipperID) {
        super.username = username;
        this.realName = realName;
        this.email = email;
        this.passwordUpdated = passwordUpdated;
        this.ranchAccess = ranchAccess;
        this.permissions = permissions;
        this.passwordReset = passwordReset;
        this.shipperID = shipperID;
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

    public String getShipperID() {
        return Strings.isNullOrEmpty(this.shipperID) ? "" : this.shipperID;
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
