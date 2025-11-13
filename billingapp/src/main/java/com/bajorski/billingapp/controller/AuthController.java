package com.bajorski.billingapp.controller;

import com.bajorski.billingapp.io.AuthRequest;
import com.bajorski.billingapp.io.AuthResponse;
import com.bajorski.billingapp.service.UserService;
import com.bajorski.billingapp.service.impl.AppUserDetailsService;
import com.bajorski.billingapp.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final AppUserDetailsService userDetailsService;

    private final UserService userService;

    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request, HttpServletResponse response) { // Dodaj HttpServletResponse
        authenticate(request.getEmail(), request.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        final String accessToken = jwtUtil.generateToken(userDetails);
        final String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true); // Ustaw na true, jeśli używasz HTTPS
        refreshTokenCookie.setPath("/"); // Dostępne na całej stronie
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7 dni (taki sam czas jak ważność tokena)
        response.addCookie(refreshTokenCookie);

        return new AuthResponse(request.getEmail(), accessToken, userService.getUserRole(request.getEmail()));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0); // Usuń ciasteczko
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String refreshToken = null;
        if (request.getCookies() != null) {
            refreshToken = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals("refreshToken"))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        }

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token not found");
        }

        try {
            String email = jwtUtil.extractUsernameFromRefreshToken(refreshToken); // Zaimplementuj to
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            if (jwtUtil.validateRefreshToken(refreshToken, userDetails)) {
                String newAccessToken = jwtUtil.generateToken(userDetails);
                return ResponseEntity.ok(Map.of("token", newAccessToken));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
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
