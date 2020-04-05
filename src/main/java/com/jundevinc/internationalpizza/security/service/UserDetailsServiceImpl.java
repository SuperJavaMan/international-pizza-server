package com.jundevinc.internationalpizza.security.service;

import com.jundevinc.internationalpizza.security.model.User;
import com.jundevinc.internationalpizza.security.model.UserPrincipal;
import com.jundevinc.internationalpizza.security.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Oleg Pavlyukov
 * on 03.12.2019
 * cpabox777@gmail.com
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository repository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername() invoked");
        User user = repository.findUserByUsername(username)
                                .orElseThrow(() -> {
                                    log.debug("User not found with username -> " + username);
                                    return new UsernameNotFoundException("User with name\"" + username + "\" not found");
                                });
        log.debug("User with username='" + username + "' is exists");
        return UserPrincipal.build(user);
    }
}
