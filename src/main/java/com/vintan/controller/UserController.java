package com.vintan.controller;

import com.vintan.domain.User;
import com.vintan.dto.response.user.*;
import com.vintan.dto.request.user.UserLoginRequestDto;
import com.vintan.dto.request.user.UserRegisterRequestDto;
import com.vintan.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for user authentication and session management.
 * Handles user registration, login, logout, session check, and duplicate ID check.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/login")
public class UserController {

    private final UserService userService;

    /**
     * POST /auth/login/register
     * Registers a new user with the provided information.
     *
     * @param requestDto registration request data
     * @return RegisterResponseDto indicating success (1) or failure (0)
     */
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@Valid @RequestBody UserRegisterRequestDto requestDto) {
        try {
            userService.register(requestDto);
            return ResponseEntity.ok(new RegisterResponseDto(1));
        } catch (IllegalArgumentException e) {
            // Return 400 Bad Request if registration fails
            return ResponseEntity.badRequest().body(new RegisterResponseDto(0));
        }
    }

    /**
     * GET /auth/login/duplicate/{userId}
     * Checks if a given user ID is already taken.
     *
     * @param userId the ID to check
     * @return DuplicatedCheckResponseDto with 1 if duplicate, 0 otherwise
     */
    @GetMapping("/duplicate/{userId}")
    public ResponseEntity<DuplicatedCheckResponseDto> checkUserIdDuplicate(@PathVariable String userId) {
        boolean isDuplicate = userService.isUserDuplicated(userId);
        return ResponseEntity.ok(new DuplicatedCheckResponseDto(isDuplicate ? 1 : 0));
    }

    /**
     * POST /auth/login/login
     * Authenticates a user and creates a session.
     *
     * @param requestDto login request containing user ID and password
     * @param request HttpServletRequest for session handling
     * @return UserLoginResponseDto indicating success (1) or failure (0)
     */
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@RequestBody UserLoginRequestDto requestDto, HttpServletRequest request) {
        try {
            // Authenticate the user
            User user = userService.login(requestDto.getId(), requestDto.getPassword());

            // Create a new session and store logged-in user info
            HttpSession session = request.getSession(true);
            session.setAttribute("loggedInUser", new SessionUserDto(user));

            return ResponseEntity.ok(new UserLoginResponseDto(1));
        } catch (IllegalArgumentException e) {
            // Return 400 Bad Request if login fails
            return ResponseEntity.badRequest().body(new UserLoginResponseDto(0));
        }
    }

    /**
     * POST /auth/login/logout
     * Logs out the current user by invalidating the session.
     *
     * @param request HttpServletRequest for session access
     * @return LogoutResponseDto indicating success (1)
     */
    @PostMapping("logout")
    public ResponseEntity<LogoutResponseDto> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // Destroy session
        }
        return ResponseEntity.ok(new LogoutResponseDto(1));
    }

    /**
     * GET /auth/login/session
     * Retrieves information about the current logged-in user.
     *
     * @param request HttpServletRequest for session access
     * @return UserInfoResponseDto containing login status and business user flag
     */
    @GetMapping("/session")
    public ResponseEntity<UserInfoResponseDto> getUserInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        // Return 401 Unauthorized if no session or user info
        if (session == null || session.getAttribute("loggedInUser") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new UserInfoResponseDto(0, 0, ""));
        }

        // Retrieve user info from session
        SessionUserDto sessionUserDto = (SessionUserDto) session.getAttribute("loggedInUser");

        // Determine if the user is a business user
        int isBusiness = (sessionUserDto.getBusinessNumber() > 0) ? 1 : 0;
        String name = sessionUserDto.getName();

        return ResponseEntity.ok(new UserInfoResponseDto(1, isBusiness, name));
    }
}
