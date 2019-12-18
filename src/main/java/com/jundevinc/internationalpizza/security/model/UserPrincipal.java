package com.jundevinc.internationalpizza.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Oleg Pavlyukov
 * on 03.12.2019
 * cpabox777@gmail.com
 */
@Data
@ToString
@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    private long id;
    private String username;
    @JsonIgnore
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public static UserPrincipal build(User user) {
        List<GrantedAuthority> authorities = user.getUserRoles().stream().map(role ->
            new SimpleGrantedAuthority(role.getUserRole().name())
        ).collect(Collectors.toList());
        return new UserPrincipal(
                    user.getId(),
                    user.getUsername(),
                    user.getPassword(),
                    authorities
        );    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPrincipal)) return false;
        UserPrincipal that = (UserPrincipal) o;
        return id == that.id &&
                username.equals(that.username) &&
                password.equals(that.password) &&
                authorities.equals(that.authorities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, authorities);
    }
}
