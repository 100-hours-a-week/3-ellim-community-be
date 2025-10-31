package gguip1.community.domain.auth.controller;

import gguip1.community.domain.auth.dto.AuthRequest;
import gguip1.community.domain.auth.dto.AuthResponse;
import gguip1.community.domain.auth.dto.TokenResponse;
import gguip1.community.domain.auth.service.AuthService;
import gguip1.community.global.context.SecurityContext;
import gguip1.community.global.exception.ErrorCode;
import gguip1.community.global.exception.ErrorException;
import gguip1.community.global.response.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/auth")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody AuthRequest authRequest,
                                                           HttpServletResponse response) {
        AuthResponse authResponse = authService.login(authRequest);

        String refreshToken = authService.generateAndSaveTokens(authResponse.userId());

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(14 * 24 * 3600); // 만료 시간은 환경 변수 설정으로 대체 가능
        response.addCookie(cookie);

        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success("login_success", authResponse)
        );
    }

    @DeleteMapping("/auth")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        SecurityContext.clear();

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refreshTokens(@CookieValue(value = "refreshToken", required = false) String refreshToken,
                                                           HttpServletResponse response) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new ErrorException(ErrorCode.UNAUTHORIZED);
        }

        TokenResponse tokenResponse = authService.refreshTokens(refreshToken);

        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.success("token_refresh_success", tokenResponse)
        );
    }
}
