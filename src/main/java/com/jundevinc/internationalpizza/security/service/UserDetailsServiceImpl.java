package com.jundevinc.internationalpizza.security.service;

import com.jundevinc.internationalpizza.security.model.User;
import com.jundevinc.internationalpizza.security.model.UserPrincipal;
import com.jundevinc.internationalpizza.security.repository.UserRepository;
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
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository repository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = repository.findUserByUsername(s)
                                .orElseThrow(() -> new UsernameNotFoundException("User with name\"" + s + "\" not found"));
        return UserPrincipal.build(user);
    }
}
