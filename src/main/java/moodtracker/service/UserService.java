package moodtracker.service;

import moodtracker.entity.User;
import moodtracker.entity.UserRole;
import moodtracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(String name, String email, String password, UserRole role) {
    if (userRepository.existsByEmail(email))
        throw new RuntimeException("Email already in use");

    // Οι θεραπευτές περιμένουν έγκριση
    boolean isActive = role != UserRole.therapist;

    User user = User.builder()
        .name(name)
        .email(email)
        .passwordHash(passwordEncoder.encode(password))
        .role(role)
        .isActive(isActive)
        .build();

    return userRepository.save(user);
}

    public User findById(UUID id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<User> findByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }
}