package edu.csumb.spring19.capstone.dto.user;

import edu.csumb.spring19.capstone.models.PLUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Calendar;
import java.util.List;

public class UserInfoSend extends UserDTO {
    private String realName;
    private String email;
    private Calendar passwordUpdated;
    private List<SimpleGrantedAuthority> permissions;

    public UserInfoSend(PLUser user) {
        super.username = user.getUsername();
        this.realName = user.getRealName();
        this.email = user.getEmail();
        this.passwordUpdated = user.getPasswordUpdated();
        this.permissions = user.getPermissions();
    }

    public UserInfoSend(String username, String realName, String email, Calendar passwordUpdated,
                        List<SimpleGrantedAuthority> permissions) {
        super.username = username;
        this.realName = realName;
        this.email = email;
        this.passwordUpdated = passwordUpdated;
        this.permissions = permissions;
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

    public List<SimpleGrantedAuthority> getPermissions() {
        return permissions;
    }
}
