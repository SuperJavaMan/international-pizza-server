package com.jundevinc.internationalpizza.security.controller;

import com.jundevinc.internationalpizza.api.model.Customer;
import com.jundevinc.internationalpizza.api.repository.CustomerRepository;
import com.jundevinc.internationalpizza.security.jwt.JwtTokenProvider;
import com.jundevinc.internationalpizza.security.model.*;
import com.jundevinc.internationalpizza.security.repository.RoleRepository;
import com.jundevinc.internationalpizza.security.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
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
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private AuthenticationManager authManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder encoder;
    private JwtTokenProvider tokenProvider;
    private CustomerRepository customerRepository;

    @Autowired
    public AuthController(AuthenticationManager authManager,
                          UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder encoder,
                          JwtTokenProvider tokenProvider,
                          CustomerRepository customerRepository) {
        this.authManager = authManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.tokenProvider = tokenProvider;
        this.customerRepository = customerRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginForm loginForm) {
        log.info("Method login invocation");
        log.debug("Login with the name '" + loginForm.getUsername() + "' and password '" + loginForm.getPassword() + "'");

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.generateJwtToken(authentication);
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(new JwtResponse(token, userPrincipal.getUsername(), userPrincipal.getAuthorities()));
    }

    @PostMapping("/reg")
    public ResponseEntity<?> register(@RequestBody RegForm regForm) {
        log.info("Method reg invocation");
        log.debug("Reg with the name '" + regForm.getUsername() + "' and password '" + regForm.getPassword() + "'");

        if (userRepository.existsUserByUsername(regForm.getUsername())) {
            log.debug("User with that name already exist");
            return ResponseEntity.badRequest().body("This username is already taken! Choose another one!");
        }
        User user = new User(regForm.getUsername(),
                encoder.encode(regForm.getPassword()));
        Set<Role> defaultRoles = new HashSet<>();
        defaultRoles.add(roleRepository.findRoleByUserRole(Roles.ROLE_USER));
        user.setUserRoles(defaultRoles);
        userRepository.save(user);

        Customer customer = new Customer(regForm.getUsername(), "Default card number");
        customerRepository.save(customer);
        return ResponseEntity.ok("User registered successfully!");
    }
}
