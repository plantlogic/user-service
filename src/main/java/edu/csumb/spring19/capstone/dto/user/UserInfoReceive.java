package edu.csumb.spring19.capstone.dto.user;

import com.google.common.base.Strings;

import java.util.List;

public class UserInfoReceive extends UserDTO {
    protected String realName;
    protected String email;
    protected List<String> permissions;

    public UserInfoReceive(String username, String realName, String email, List<String> permissions) {
        super.username = username;
        this.realName = realName;
        this.email = email;
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

    public List<String> getPermissions() {
        return permissions;
    }

    public Boolean anyEmptyVal() {
        return Strings.isNullOrEmpty(super.username)
              || Strings.isNullOrEmpty(this.realName)
              || Strings.isNullOrEmpty(this.email)
              || permissions == null;
    }
}
