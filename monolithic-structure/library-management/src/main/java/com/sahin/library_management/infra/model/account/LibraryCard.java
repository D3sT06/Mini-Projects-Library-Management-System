package com.sahin.library_management.infra.model.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sahin.library_management.infra.auth.CustomAuthorityDeserializer;
import com.sahin.library_management.infra.enums.AccountFor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class LibraryCard implements UserDetails {

    private String barcode;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private Long issuedAt;
    private Boolean active;
    private AccountFor accountFor;
    private Set<AccountLoginType> loginTypes;

    @JsonDeserialize(using = CustomAuthorityDeserializer.class)
    @Override
    @JsonIgnore
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
    @JsonIgnore
    public String getUsername() {
        return getBarcode();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return getActive();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return getActive();
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return getActive();
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return getActive();
    }
}
