package gguip1.community.domain.auth.service;

import gguip1.community.domain.auth.repository.RefreshTokenRepository;
import gguip1.community.domain.auth.dto.AuthRequest;
import gguip1.community.domain.auth.dto.AuthResponse;
import gguip1.community.domain.auth.dto.TokenResponse;
import gguip1.community.domain.auth.entity.RefreshToken;
import gguip1.community.domain.user.entity.User;
import gguip1.community.domain.user.mapper.UserMapper;
import gguip1.community.domain.user.repository.UserRepository;
import gguip1.community.global.context.SecurityContext;
import gguip1.community.global.exception.ErrorCode;
import gguip1.community.global.exception.ErrorException;
import gguip1.community.global.jwt.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    private static final int ACCESS_TOKEN_EXPIRATION = 15 * 60; // 액세스 토큰 만료 시간 (15분)
    private static final int REFRESH_TOKEN_EXPIRATION = 14 * 24 * 3600; // 리프레시 토큰 만료 시간 (14일)

    @Transactional
    public AuthResponse login(AuthRequest authRequest) {
        User user = userRepository.findByEmail(authRequest.email())
                .orElseThrow(() -> new ErrorException(ErrorCode.INVALID_CREDENTIALS)); // 이메일로 사용자 조회

        if (!checkPassword(authRequest.password(), user.getPassword())) {
            throw new ErrorException(ErrorCode.INVALID_CREDENTIALS);
        } // 조회한 사용자와 비밀번호 비교

        return userMapper.toAuthResponse(user, jwtProvider.createAccessToken(user.getUserId())); // 사용자 정보를 AuthResponse로 매핑하여 반환
    }

    public TokenResponse refreshTokens(String refreshToken) {
        RefreshToken refreshTokenEntity = refreshTokenRepository.findByTokenAndRevokedFalse(refreshToken).orElse(null); // 활성화된 리프레시 토큰 조회

        if (refreshTokenEntity == null || refreshTokenEntity.getExpiresAt().isBefore(Instant.now())) {
            throw new ErrorException(ErrorCode.UNAUTHORIZED);
        } // 토큰이 없거나 만료된 경우 예외 발생

        Long userId = refreshTokenEntity.getUserId(); // 사용자 ID 추출

        return new TokenResponse(jwtProvider.createAccessToken(userId)); // 새로운 액세스 토큰 생성 및 반환
    }

    // 비밀번호 확인 메서드
    private boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    // 토큰 생성 및 저장 메서드
    public String generateAndSaveTokens(Long userId) {
        // 액세스 토큰 및 리프레시 토큰 생성
//        String accessToken = jwtProvider.createAccessToken(userId);
        String refreshToken = Base64.getUrlEncoder().encodeToString(UUID.randomUUID().toString().getBytes());

        // 리프레시 토큰 엔티티 생성 및 저장
        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setUserId(userId);
        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setExpiresAt(Instant.now().plusSeconds(REFRESH_TOKEN_EXPIRATION));
        refreshTokenEntity.setRevoked(false);
        refreshTokenRepository.save(refreshTokenEntity);

        return refreshToken;
    }
}
