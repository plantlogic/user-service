package edu.csumb.spring19.capstone.dto.user;

import java.util.List;

public class UserInfoReceiveEdit extends UserInfoReceive {
    private String initialUsername;

    public UserInfoReceiveEdit(String username, String realName, String email, List<String> permissions, String initialUsername) {
        super(username, realName, email, permissions);
        this.initialUsername = initialUsername;
    }

    public Boolean usernameChanged() {
        return !initialUsername.toLowerCase().equals(super.getUsername().toLowerCase());
    }

    public String getInitialUsername() {
        return initialUsername;
    }
}
