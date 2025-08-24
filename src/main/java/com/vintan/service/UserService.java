package com.vintan.service;

import com.vintan.domain.User;
import com.vintan.dto.request.user.UserRegisterRequestDto;
import com.vintan.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Register a new user.
     * Password is encoded before saving.
     */
    @Transactional
    public void register(UserRegisterRequestDto userRegisterRequestDto) {
        String encodedPassword = passwordEncoder.encode(userRegisterRequestDto.getPassword());

        User newUser = User.builder()
                .id(userRegisterRequestDto.getId())
                .name(userRegisterRequestDto.getName())
                .password(encodedPassword)
                .email(userRegisterRequestDto.getEmail())
                .businessNumber(userRegisterRequestDto.getBusinessNumber())
                .build();

        userRepository.save(newUser);
    }

    /**
     * Authenticate user login.
     * Throws exception if user not found or password mismatch.
     */
    public User login(String id, String password) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Incorrect password");
        }

        return user;
    }

    /**
     * Check if a user ID is already registered.
     */
    public boolean isUserDuplicated(String id) {
        return userRepository.findById(id).isPresent();
    }
}
