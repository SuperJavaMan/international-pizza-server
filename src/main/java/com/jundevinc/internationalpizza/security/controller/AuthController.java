package com.jundevinc.internationalpizza.security.controller;

import com.jundevinc.internationalpizza.security.jwt.JwtTokenProvider;
import com.jundevinc.internationalpizza.security.model.*;
import com.jundevinc.internationalpizza.security.repository.RoleRepository;
import com.jundevinc.internationalpizza.security.repository.UserRepository;
import com.jundevinc.internationalpizza.security.service.UploadFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Oleg Pavlyukov
 * on 03.12.2019
 * cpabox777@gmail.com
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthenticationManager authManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder encoder;
    private JwtTokenProvider tokenProvider;

    @Autowired
    public AuthController(AuthenticationManager authManager,
                          UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder encoder,
                          JwtTokenProvider provider) {
        this.authManager = authManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.tokenProvider = provider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginForm loginForm) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.generateJwtToken(authentication);
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(new JwtResponse(token, userPrincipal.getUsername(), userPrincipal.getAuthorities()));
    }

    @PostMapping("/reg")
    public ResponseEntity<?> register(@ModelAttribute RegForm regForm) {
        if (userRepository.existsUserByUsername(regForm.getUsername()))
            return ResponseEntity.badRequest().body("This username is already taken! Choose another one!");
        User user = new User(regForm.getUsername(),
                encoder.encode(regForm.getPassword()),
                UploadFileUtil.getStoragePath(regForm.getFile().getOriginalFilename()));
        Set<Role> defaultRoles = new HashSet<>();
        defaultRoles.add(roleRepository.findRoleByUserRole(Roles.ROLE_USER));
        user.setUserRoles(defaultRoles);
        userRepository.save(user);
        return ResponseEntity.ok().body("User registered successfully!");
    }
}
