package moodtracker.controller;

import moodtracker.entity.User;
import moodtracker.entity.UserRole;
import moodtracker.security.JwtService;
import moodtracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        User user = userService.register(
            body.get("name"),
            body.get("email"),
            body.get("password"),
            UserRole.valueOf(body.get("role"))
        );
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
        return ResponseEntity.ok(Map.of(
            "token", token,
            "role", user.getRole().name(),
            "name", user.getName()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                body.get("email"),
                body.get("password")
            )
        );
        User user = userService.findByEmail(body.get("email"));
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
        return ResponseEntity.ok(Map.of(
            "token", token,
            "role", user.getRole().name(),
            "name", user.getName()
        ));
    }
}