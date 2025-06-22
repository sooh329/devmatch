package com.dongyang.devmatch.api;

import com.dongyang.devmatch.dto.LoginRequestDto;
import com.dongyang.devmatch.dto.SignupRequestDto;
import com.dongyang.devmatch.entity.User;
import com.dongyang.devmatch.repository.UserRepository;
import com.dongyang.devmatch.security.JwtTokenProvider;
import com.dongyang.devmatch.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApiController {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response) {
        Optional<User> userOpt = userRepository.findByUsername(requestDto.getUsername());

        if (userOpt.isEmpty() || !passwordEncoder.matches(requestDto.getPassword(), userOpt.get().getPassword())) {
            return ResponseEntity.badRequest().body("아이디 또는 비밀번호가 잘못되었습니다.");
        }

        String token = jwtTokenProvider.createToken(userOpt.get().getUsername());

        // 쿠키에 JWT 저장
        ResponseCookie cookie = ResponseCookie.from("jwtToken", token)
                .httpOnly(true)
                .path("/")
                .maxAge(60 * 60 * 24) // 1일
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("로그인 성공");
    }

    // 로그아웃 API
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // jwtToken 쿠키를 무효화
        ResponseCookie deleteCookie = ResponseCookie.from("jwtToken", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());

        try {
            response.sendRedirect("/");
        } catch (IOException e) {
            throw new RuntimeException("리다이렉트 실패", e);
        }

        return null;
    }

    // 회원가입 API
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDto requestDto) {
        if (!requestDto.getPassword().equals(requestDto.getPasswordConfirm())) {
            return ResponseEntity.badRequest().body("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        boolean success = authService.signup(requestDto);
        if (success) {
            return ResponseEntity.ok("회원가입 성공");
        } else {
            return ResponseEntity.badRequest().body("이미 존재하는 사용자입니다.");
        }
    }
}
