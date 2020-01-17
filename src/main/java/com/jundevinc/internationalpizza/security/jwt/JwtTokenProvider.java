package com.jundevinc.internationalpizza.security.jwt;

import com.jundevinc.internationalpizza.security.model.UserPrincipal;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Oleg Pavlyukov
 * on 03.12.2019
 * cpabox777@gmail.com
 */
@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${uploadfile.app.jwtSecret}")
    private String secretKey;

    @Value("${uploadfile.app.jwtExpiration}")
    private int expirationTime;

    public String generateJwtToken(Authentication authentication) {
        log.info("generateJwtToken() invoked");
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        log.debug("Generate token for UserPrincipal with name -> " + user.getUsername()
                    + " and with authorities -> " + user.getAuthorities());
        return Jwts.builder()
                    .setSubject(user.getUsername())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date((new Date()).getTime() + expirationTime*1000))
                    .signWith(SignatureAlgorithm.HS512, secretKey)
                    .compact();
    }

    public boolean isJwtTokenValid(String token) {
        log.info("isJwtTokenValid() invocation");
        try {

        Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        log.debug("Token is valid");
        return true;
        } catch (Exception e) {
            log.debug("Token is not valid", e);
            e.printStackTrace();
        }
        return false;
    }

    public String getUsernameFromJwtToken(String jwtToken) {
        log.info("getUsernameFromJwtToken() invocation. Extract an username from the token");
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwtToken)
                .getBody()
                .getSubject();
    }
}
