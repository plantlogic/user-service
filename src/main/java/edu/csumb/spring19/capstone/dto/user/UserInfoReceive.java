package edu.csumb.spring19.capstone.dto.user;

import com.google.common.base.Strings;

import java.util.List;

public class UserInfoReceive extends UserDTO {
    protected String realName;
    protected String email;
    protected String password;
    protected List<String> ranchAccess;
    protected List<String> permissions;
    protected String shipperID;

    public UserInfoReceive(String username, String realName, String email, String password, List<String> ranchAccess, List<String> permissions, String shipperID) {
        super.username = username;
        this.realName = realName;
        this.email = email;
        this.password = password;
        this.ranchAccess = ranchAccess;
        this.permissions = permissions;
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

    public String getPassword() {
        return password;
    }

    public List<String> getRanchAccess() {
        return ranchAccess;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public String getShipperID() {
        return (this.hasShipperID()) ? shipperID : "";
    }

    public Boolean hasShipperID() {
        return !Strings.isNullOrEmpty(this.shipperID);
    }

    public Boolean anyEmptyVal() {
        return Strings.isNullOrEmpty(super.username)
              || Strings.isNullOrEmpty(this.realName)
              || permissions == null;
    }

    public Boolean hasBothPassAndEmail() {
        return !Strings.isNullOrEmpty(this.email) && !Strings.isNullOrEmpty(this.password);
    }

    public Boolean hasNeitherPassOrEmail() {
        return Strings.isNullOrEmpty(this.email) && Strings.isNullOrEmpty(this.password);
    }

    public void unifyStringCase() {
        username = username.toLowerCase();
        if (!Strings.isNullOrEmpty(email)) {
            email = email.toLowerCase();
        }
    }
}
