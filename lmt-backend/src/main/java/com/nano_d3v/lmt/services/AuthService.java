package com.nano_d3v.lmt.services;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.nano_d3v.lmt.api.models.User;
import com.nano_d3v.lmt.api.repositories.UserRepository;

@Service
public class AuthService {

    public final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // create a new account and sign it in
    public User register(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please choose a username");
        }
        if (password == null || password.length() < 4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password must be at least 4 characters long");
        }
        if (userRepository.findByUsername(username.trim()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This username is already taken");
        }

        User user = new User(username.trim(), passwordEncoder.encode(password));
        user.setToken(UUID.randomUUID().toString());
        return userRepository.save(user);
    }

    // verify the credentials and issue a fresh token
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username == null ? "" : username.trim())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));

        if (password == null || !passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }

        user.setToken(UUID.randomUUID().toString());
        return userRepository.save(user);
    }

    // invalidate the token so it can no longer be used
    public void logout(User user) {
        user.setToken(null);
        userRepository.save(user);
    }
}
