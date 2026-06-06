package com.ams.core.controller;

import com.ams.core.dto.*;
import com.ams.core.security.CustomUserDetailsService;
import com.ams.core.security.UserPrincipal;
import com.ams.core.security.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication and token refresh")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtils jwtUtils,
                          CustomUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Logs in a user and returns JWT access and refresh tokens.")
    public ResponseEntity<ApiResponse<LoginResponse>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateAccessToken(authentication);
        String refreshJwt = jwtUtils.generateRefreshToken(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String role = userPrincipal.getUser().getRole().name();

        LoginResponse loginResponse = new LoginResponse(
                jwt,
                refreshJwt,
                userPrincipal.getUsername(),
                role
        );

        return ResponseEntity.ok(ApiResponse.success("Login successful", loginResponse));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenRefreshResponse>> refreshToken(@Valid @RequestBody TokenRefreshRequest refreshRequest) {
        String requestRefreshToken = refreshRequest.refreshToken();

        if (jwtUtils.validateJwtToken(requestRefreshToken)) {
            String username = jwtUtils.getUsernameFromJwtToken(requestRefreshToken);
            UserPrincipal userPrincipal = (UserPrincipal) userDetailsService.loadUserByUsername(username);
            
            String newAccessToken = jwtUtils.generateAccessToken(userPrincipal);
            
            return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", new TokenRefreshResponse(newAccessToken)));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(HttpStatus.UNAUTHORIZED.value(), "Invalid or expired refresh token"));
    }
}
