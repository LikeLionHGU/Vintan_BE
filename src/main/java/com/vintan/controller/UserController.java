package com.vintan.controller;

import com.vintan.domain.User;
import com.vintan.dto.response.user.*;
import com.vintan.dto.request.user.UserLoginRequestDto;
import com.vintan.dto.request.user.UserRegisterRequestDto;
import com.vintan.repository.UserRepository;
import com.vintan.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/login")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@Valid @RequestBody UserRegisterRequestDto requestDto) {
        try {
            userService.register(requestDto);
            return ResponseEntity.ok(new RegisterResponseDto(1));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new RegisterResponseDto(0));
        }
    }

    @GetMapping("/duplicate/{userId}")
    public ResponseEntity<DuplicatedCheckResponseDto> checkUserIdDuplicate(@PathVariable String userId) {
        boolean isDuplicate = userService.isUserDuplicated(userId);
        return ResponseEntity.ok(new DuplicatedCheckResponseDto(isDuplicate ? 1 : 0));
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@RequestBody UserLoginRequestDto requestDto, HttpServletRequest request) {
        try {
            User user = userService.login(requestDto.getId(), requestDto.getPassword());

            HttpSession session = request.getSession(true);
            session.setAttribute("loggedInUser", new SessionUserDto(user));

            return ResponseEntity.ok(new UserLoginResponseDto(1));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new UserLoginResponseDto(0));
        }
    }

    @PostMapping("logout")
    public ResponseEntity<LogoutResponseDto> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok(new LogoutResponseDto(1));
    }

    @GetMapping("/session")
    public ResponseEntity<UserInfoResponseDto> getUserInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("loggedInUser") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new UserInfoResponseDto(0, 0));
        }

        SessionUserDto sessionUserDto = (SessionUserDto) session.getAttribute("loggedInUser");

        int isBusiness = (sessionUserDto.getBusinessNumber() > 0) ? 1 : 0;

        return ResponseEntity.ok(new UserInfoResponseDto(1, isBusiness));
    }
}
