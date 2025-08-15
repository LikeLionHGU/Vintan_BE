package com.vintan.controller;

import com.vintan.domain.User;
import com.vintan.dto.request.user.UserRegisterRequestDto;
import com.vintan.dto.response.user.DuplicatedCheckResponseDto;
import com.vintan.dto.response.user.RegisterResponseDto;
import com.vintan.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/login")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public RegisterResponseDto register(@Valid @RequestBody UserRegisterRequestDto requestDto) {
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        try {
            User newUser = User.builder()
                    .id(requestDto.getId())
                    .name(requestDto.getName())
                    .password(encodedPassword)
                    .email(requestDto.getEmail())
                    .businessNumber(requestDto.getBusinessNumber())
                    .build();
            userRepository.save(newUser);
            return new RegisterResponseDto(1);
        } catch (Exception e) {
            return new RegisterResponseDto(0);
        }
    }

    @GetMapping("/duplicate/{userId}")
    public DuplicatedCheckResponseDto checkUserIdDuplicate(@PathVariable String userId) {
        boolean isDuplicate = userRepository.existsById(userId);

        if (isDuplicate) {
            return new DuplicatedCheckResponseDto(1);
        } else {
            return new DuplicatedCheckResponseDto(0);
        }
    }
}
