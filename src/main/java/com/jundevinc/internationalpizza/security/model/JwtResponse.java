package com.jundevinc.internationalpizza.security.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author Oleg Pavlyukov
 * on 03.12.2019
 * cpabox777@gmail.com
 */
@Data
@NoArgsConstructor
public class JwtResponse {
    private String accessToken;
    private String type = "Bearer ";
    private String username;
    private Collection<? extends GrantedAuthority> authorities;

    public JwtResponse(String accessToken, String username, Collection<? extends GrantedAuthority> authorities) {
        this.accessToken = accessToken;
        this.username = username;
        this.authorities = authorities;
    }
}
