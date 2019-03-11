package edu.csumb.spring19.capstone.models;

import org.springframework.security.core.GrantedAuthority;

public enum PLRole implements GrantedAuthority {
    DATA_ENTRY, DATA_VIEW, DATA_EDIT, USER_MANAGEMENT, APP_ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
