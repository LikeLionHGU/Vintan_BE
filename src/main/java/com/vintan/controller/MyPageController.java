package com.vintan.controller;

import com.vintan.dto.response.mypage.MyPageResponse;
import com.vintan.service.MyPageQueryService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageQueryService myPageQueryService;

    private String currentUserId(HttpSession session) {
        Object obj = session.getAttribute("loggedInUser");
        if (obj == null) return null;
        try {
            var m = obj.getClass().getMethod("getId");
            Object id = m.invoke(obj);
            return id != null ? id.toString() : null;
        } catch (Exception ignored) {}
        if (obj instanceof String s) return s;
        return null;
    }

    @GetMapping
    public ResponseEntity<?> getMe(HttpSession session) {
        String userId = currentUserId(session);
        if (userId == null) return ResponseEntity.status(401).body("No login info");

        MyPageResponse res = myPageQueryService.getMyPage(userId);
        if (res == null) return ResponseEntity.status(404).body("User not found");
        return ResponseEntity.ok(res);
    }
}