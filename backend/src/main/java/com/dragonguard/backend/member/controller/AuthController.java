package com.dragonguard.backend.member.controller;

import com.dragonguard.backend.member.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 김승진
 * @description OAuth2를 위한 임시 컨트롤러
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @GetMapping("/refresh")
    public ResponseEntity<Object> authorize(HttpServletRequest request, HttpServletResponse response, @RequestBody String accessToken) {
        return ResponseEntity.ok(authService.refreshToken(request, response, accessToken));
    }
}
