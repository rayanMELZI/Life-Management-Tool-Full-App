package com.nano_d3v.lmt.config;

import java.io.IOException;
import java.util.Optional;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.nano_d3v.lmt.api.models.User;
import com.nano_d3v.lmt.api.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    public final UserRepository userRepository;

    public AuthInterceptor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean preHandle(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull Object handler
    ) throws IOException {
        // CORS preflight requests carry no credentials
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            Optional<User> user = userRepository.findByToken(header.substring("Bearer ".length()));
            if (user.isPresent()) {
                request.setAttribute("user", user.get());
                return true;
            }
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"message\":\"Please sign in to continue\"}");
        return false;
    }
}
