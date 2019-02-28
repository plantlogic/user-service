package edu.csumb.spring19.capstone.dto.auth;

public class AuthDTO extends TokenDTO {
    public String name;

    public AuthDTO(TokenDTO input, String name) {
        super(input.getExpiration(), input.getToken());
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
