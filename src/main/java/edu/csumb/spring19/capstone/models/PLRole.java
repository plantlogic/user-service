package edu.csumb.spring19.capstone.models;

import org.springframework.security.core.GrantedAuthority;

public enum PLRole implements GrantedAuthority {
    DATA_ENTRY, VIEW_DATA, EDIT_DATA, USER_MANAGEMENT;

    public String getAuthority() {
        return name();
    }
}
