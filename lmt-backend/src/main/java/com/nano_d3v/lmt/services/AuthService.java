package com.nano_d3v.lmt.services;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.nano_d3v.lmt.api.models.User;
import com.nano_d3v.lmt.api.repositories.UserRepository;

@Service
public class AuthService {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private static final Duration TOKEN_LIFETIME = Duration.ofDays(30);

    public final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // create a new account and sign it in
    public User register(String name, String email, String password) {
        if (name == null || name.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please enter your name");
        }
        String normalizedEmail = normalizeEmail(email);
        if (!EMAIL_PATTERN.matcher(normalizedEmail).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please enter a valid email address");
        }
        if (password == null || password.length() < 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password must be at least 8 characters long");
        }
        if (userRepository.findByEmail(normalizedEmail).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "An account with this email already exists");
        }

        User user = new User(normalizedEmail, name.trim(), passwordEncoder.encode(password));
        issueToken(user);
        return userRepository.save(user);
    }

    // verify the credentials and issue a fresh token
    public User login(String email, String password) {
        User user = userRepository.findByEmail(normalizeEmail(email))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

        if (password == null || !passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        issueToken(user);
        return userRepository.save(user);
    }

    // invalidate the token so it can no longer be used
    public void logout(User user) {
        user.setToken(null);
        user.setTokenExpiresAt(null);
        userRepository.save(user);
    }

    private void issueToken(User user) {
        user.setToken(UUID.randomUUID().toString());
        user.setTokenExpiresAt(Instant.now().plus(TOKEN_LIFETIME));
    }

    private String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }
}
