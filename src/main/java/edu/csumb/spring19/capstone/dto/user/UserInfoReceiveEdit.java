package edu.csumb.spring19.capstone.dto.user;

import com.google.common.base.Strings;

import java.util.List;

public class UserInfoReceiveEdit extends UserInfoReceive {
    private String initialUsername;

    public UserInfoReceiveEdit(String username, String realName, String email, List<String> permissions, String initialUsername) {
        super(username, realName, email, permissions);
        this.initialUsername = initialUsername;
    }

    public Boolean usernameChanged() {
        return !initialUsername.equalsIgnoreCase(super.getUsername());
    }

    public String getInitialUsername() {
        return initialUsername;
    }

    public Boolean anyEmptyVal() {
        return super.anyEmptyVal() || Strings.isNullOrEmpty(initialUsername);
    }
}
