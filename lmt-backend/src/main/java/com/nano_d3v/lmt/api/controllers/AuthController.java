package com.nano_d3v.lmt.api.controllers;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nano_d3v.lmt.api.models.User;
import com.nano_d3v.lmt.services.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    public record Credentials(String username, String password) {}

    public final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Credentials credentials) {
        User user = authService.register(credentials.username(), credentials.password());
        return Map.of("username", user.getUsername(), "token", user.getToken());
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Credentials credentials) {
        User user = authService.login(credentials.username(), credentials.password());
        return Map.of("username", user.getUsername(), "token", user.getToken());
    }

    @PostMapping("/logout")
    public String logout(@RequestAttribute("user") User user) {
        authService.logout(user);
        return "Logged out successfully";
    }

    // lets the client check whether its stored token is still valid
    @GetMapping("/me")
    public Map<String, String> me(@RequestAttribute("user") User user) {
        return Map.of("username", user.getUsername());
    }
}
