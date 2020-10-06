package com.sahin.library_management.infra.model.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sahin.library_management.infra.enums.AccountFor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class LibraryCard implements UserDetails {
    private String barcode;

    @JsonIgnore
    private String password;

    private Long issuedAt;
    private Boolean active;
    private AccountFor accountFor;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        switch (accountFor) {
            case MEMBER:
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
                break;
            case LIBRARIAN:
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_LIBRARIAN"));
                break;
            default:
                throw new IllegalStateException("Invalid account type");
        }

        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return getBarcode();
    }

    @Override
    public boolean isAccountNonExpired() {
        return getActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return getActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return getActive();
    }

    @Override
    public boolean isEnabled() {
        return getActive();
    }
}
