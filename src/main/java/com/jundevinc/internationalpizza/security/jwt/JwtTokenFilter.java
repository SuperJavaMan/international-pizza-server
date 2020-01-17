package com.jundevinc.internationalpizza.security.jwt;

import com.jundevinc.internationalpizza.security.service.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Oleg Pavlyukov
 * on 03.12.2019
 * cpabox777@gmail.com
 */
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider provider;
    @Autowired
    private UserDetailsServiceImpl service;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("doFilterInternal() invoked");
        try {
        String jwtToken = getJwtToken(httpServletRequest);
        if (getJwtToken(httpServletRequest)!= null && provider.isJwtTokenValid(jwtToken)) {
            log.debug("Extracted token is valid");
            String username = provider.getUsernameFromJwtToken(jwtToken);
            UserDetails userPrincipal = service.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userPrincipal, null, userPrincipal.getAuthorities()
            );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            log.debug("Set authentication to the security context");
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Can NOT set user authentication", e);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getJwtToken(HttpServletRequest request) {
        log.info("getJwtToken() invocation. Try to extract token from request's header");
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            log.debug("Authorization header is exists. Extract token.");
            return authHeader.replace("Bearer ", "");
        } else {
            log.debug("Invalid Authorization header -> " + authHeader);
            return null;
        }
    }
}
