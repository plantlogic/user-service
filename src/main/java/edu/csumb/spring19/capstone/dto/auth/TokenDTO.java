package edu.csumb.spring19.capstone.dto.auth;

import java.util.Date;

public class TokenDTO {
    public Date expiration;
    public String token;

    public TokenDTO(Date expiration, String token) {
        // Javascript date format
        this.expiration = expiration;
        this.token = token;
    }

    public Date getExpiration() {
        return expiration;
    }

    public String getToken() {
        return token;
    }
}
