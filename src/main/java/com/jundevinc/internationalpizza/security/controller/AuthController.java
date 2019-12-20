package com.jundevinc.internationalpizza.security.controller;

import com.jundevinc.internationalpizza.api.model.Customer;
import com.jundevinc.internationalpizza.api.repository.CustomerRepository;
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
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/auth")
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
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.generateJwtToken(authentication);
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(new JwtResponse(token, userPrincipal.getUsername(), userPrincipal.getAuthorities()));
    }

    @PostMapping("/reg")
    public ResponseEntity<?> register(@RequestBody RegForm regForm) {
        if (userRepository.existsUserByUsername(regForm.getUsername()))
            return ResponseEntity.badRequest().body("This username is already taken! Choose another one!");
        User user = new User(regForm.getUsername(),
                encoder.encode(regForm.getPassword()));
        Set<Role> defaultRoles = new HashSet<>();
        defaultRoles.add(roleRepository.findRoleByUserRole(Roles.ROLE_USER));
        user.setUserRoles(defaultRoles);
        userRepository.save(user);

        Customer customer = new Customer(regForm.getUsername(), "Card number in AuthController");
        customerRepository.save(customer);
        return ResponseEntity.ok().body("User registered successfully!");
    }
}
