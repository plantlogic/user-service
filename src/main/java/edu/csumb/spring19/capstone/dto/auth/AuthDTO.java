package edu.csumb.spring19.capstone.dto.auth;

import edu.csumb.spring19.capstone.dto.user.UserInfoSend;

public class AuthDTO extends TokenDTO {
    public UserInfoSend user;

    public AuthDTO(TokenDTO input, UserInfoSend user) {
        super(input.getExpiration(), input.getToken());
        this.user = user;
    }

    public UserInfoSend getUser() {
        return user;
    }
}
