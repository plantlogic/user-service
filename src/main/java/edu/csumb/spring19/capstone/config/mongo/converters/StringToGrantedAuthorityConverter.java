package edu.csumb.spring19.capstone.config.mongo.converters;

import edu.csumb.spring19.capstone.models.PLRole;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

// Used to convert Roles to an attribute that can be saved to and loaded from the database
@Component
public class StringToGrantedAuthorityConverter implements Converter<String, GrantedAuthority> {
    @Override
    public GrantedAuthority convert(String role) {
        if (role != null) return PLRole.valueOf(role);
        else return null;
    }
}
