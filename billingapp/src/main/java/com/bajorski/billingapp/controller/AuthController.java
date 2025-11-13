package com.bajorski.billingapp.controller;

import com.bajorski.billingapp.io.AuthRequest;
import com.bajorski.billingapp.io.AuthResponse;
import com.bajorski.billingapp.service.UserService;
import com.bajorski.billingapp.service.impl.AppUserDetailsService;
import com.bajorski.billingapp.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final AppUserDetailsService userDetailsService;

    private final UserService userService;

    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) throws DisabledException {
        authenticate(request.getEmail(), request.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        final String jwtToken = jwtUtil.generateToken(userDetails);

        return new AuthResponse(request.getEmail(), jwtToken, userService.getUserRole(request.getEmail()));
    }

    private void authenticate(String email, String password) throws DisabledException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new DisabledException("User is disabled");
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email ot password is incorrect");
        }
    }
}
