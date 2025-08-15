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

    @Transactional
    public User register(UserRegisterRequestDto userRegisterRequestDto) {
        String encodedPassword = passwordEncoder.encode(userRegisterRequestDto.getPassword());
        User newUser = User.builder()
                .id(userRegisterRequestDto.getId())
                .name(userRegisterRequestDto.getName())
                .password(encodedPassword)
                .email(userRegisterRequestDto.getEmail())
                .businessNumber(userRegisterRequestDto.getBusinessNumber())
                .build();

        return userRepository.save(newUser);
    }

    public User login(String id, String password) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("no such user"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("wrong password");
        }
        return user;
    }

    public boolean isUserDuplicated(String id) {
        return userRepository.findById(id).isPresent();
    }
}
