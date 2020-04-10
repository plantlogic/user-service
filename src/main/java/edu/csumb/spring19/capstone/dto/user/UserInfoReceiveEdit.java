package edu.csumb.spring19.capstone.dto.user;

import com.google.common.base.Strings;

import java.util.List;

public class UserInfoReceiveEdit extends UserInfoReceive {
    private String initialUsername;

    public UserInfoReceiveEdit(String username, String realName, String email, String password, List<String> ranchAccess,
                               List<String> permissions, String initialUsername, String shipperID) {
        super(username, realName, email, password, ranchAccess, permissions, shipperID);
        this.initialUsername = initialUsername;
    }

    public Boolean usernameChanged() {
        return !initialUsername.equalsIgnoreCase(super.getUsername());
    }

    public String getInitialUsername() {
        return initialUsername;
    }

    @Override
    public Boolean anyEmptyVal() {
        return super.anyEmptyVal() || Strings.isNullOrEmpty(initialUsername);
    }
}
