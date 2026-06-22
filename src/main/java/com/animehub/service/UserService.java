package com.animehub.service;

import com.animehub.model.User;
import com.animehub.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * @throws IllegalArgumentException kung kuha na ang username/email -
     * hinuhuli ito sa AuthController para maipakita bilang form error
     * imbes na 500 server error.
     */
    public User registerUser(String username, String email, String rawPassword) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Kuha na ang username na ito.");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("May account na gamit ang email na ito.");
        }

        // BINIBIGYAN diin: ENCODE muna bago i-save - kailanman huwag i-store
        // ang plain text password.
        String hashedPassword = passwordEncoder.encode(rawPassword);
        User user = new User(username, email, hashedPassword);
        return userRepository.save(user);
    }
}
