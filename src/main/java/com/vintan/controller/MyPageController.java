package com.vintan.controller;

import com.vintan.dto.response.mypage.MyPageResponse;
import com.vintan.service.MyPageQueryService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for retrieving information about the currently logged-in user.
 * Provides a single endpoint to fetch the user's "My Page" data.
 */
@RestController
@RequestMapping("/api/me") // Base URL for current user operations
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageQueryService myPageQueryService;

    /**
     * Retrieves the current logged-in user's ID from the HttpSession.
     * Handles both object types with a "getId" method and plain String IDs.
     *
     * @param session the current HttpSession
     * @return user ID as String, or null if no user is logged in
     */
    private String currentUserId(HttpSession session) {
        Object obj = session.getAttribute("loggedInUser");
        if (obj == null) return null;

        try {
            // Try to invoke getId() if present
            var m = obj.getClass().getMethod("getId");
            Object id = m.invoke(obj);
            return id != null ? id.toString() : null;
        } catch (Exception ignored) {}

        // Fallback if the session object is already a String
        if (obj instanceof String s) return s;

        return null;
    }

    /**
     * GET /api/me
     * Retrieves the MyPageResponse for the currently logged-in user.
     *
     * @param session HttpSession to get current user
     * @return ResponseEntity containing MyPageResponse if user exists,
     *         or appropriate HTTP error status if not logged in or not found
     */
    @GetMapping
    public ResponseEntity<?> getMe(HttpSession session) {
        String userId = currentUserId(session);

        // Return 401 Unauthorized if user is not logged in
        if (userId == null) return ResponseEntity.status(401).body("No login info");

        // Fetch MyPage data from service
        MyPageResponse res = myPageQueryService.getMyPage(userId);

        // Return 404 Not Found if the user does not exist
        if (res == null) return ResponseEntity.status(404).body("User not found");

        // Return 200 OK with the user's MyPage data
        return ResponseEntity.ok(res);
    }
}
