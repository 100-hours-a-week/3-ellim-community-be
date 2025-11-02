package gguip1.community.global.filter;

import gguip1.community.global.context.SecurityContext;
import gguip1.community.global.exception.ErrorCode;
import gguip1.community.global.exception.ErrorException;
import gguip1.community.global.jwt.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtProvider jwtProvider;

        private static final String[] EXCLUDED_PATHS = {
                "/auth", // 로그인 및 로그아웃 엔드포인트
                "/users", // 회원가입 엔드포인트
                "/users/check-email", // 이메일 중복 확인 엔드포인트
                "/users/check-nickname", // 닉네임 중복 확인 엔드포인트
                "/terms", // 이용약관
                "/privacy", // 개인정보처리방침
                "/error", // 에러 처리 엔드포인트
                "/auth/refresh" // 토큰 재발급 엔드포인트
        };

        /*
         * 필터링 제외 경로 설정
         */
        @Override
        protected boolean shouldNotFilter(HttpServletRequest request) {
            String path = request.getRequestURI();

            boolean isExcluded = Arrays.stream(EXCLUDED_PATHS)
                    .anyMatch(path::contains);

            boolean imageMatched = path.startsWith("/images/");

            return isExcluded || imageMatched;
        }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            // 토큰 추출
            String token = extractTokenFromHeader(request) // 헤더에서 토큰 추출
                    .or(() -> extractTokenFromCookie(request)) // 헤더에 토큰이 없으면 쿠키에서 토큰 추출
                    .orElseThrow(() -> new ErrorException(ErrorCode.UNAUTHORIZED)); // 헤더에도 쿠키에도 토큰이 없으면 예외 발생

            // 토큰 검증 및 사용자 정보 설정
            Long userId = jwtProvider.getUserId(token);

            // SecurityContext에 사용자 정보 설정
            SecurityContext.setCurrentUserId(userId);

            // 다음 필터로 요청 전달
            filterChain.doFilter(request, response);
        } catch (ErrorException ex) {
            handlerExceptionResolver.resolveException(request, response, null, ex); // 인증 관련 예외 처리
        } catch (Exception ex) {
            handlerExceptionResolver.resolveException(request, response, null, new ErrorException(ErrorCode.INTERNAL_SERVER_ERROR)); // 기타 예외 처리
        } finally {
            SecurityContext.clear(); // 요청 처리 후 SecurityContext 정리
        }
    }

    // "Authorization" 헤더에서 토큰 추출
    // "Bearer " 접두사를 제거하여 순수 토큰 값 반환
    private Optional<String> extractTokenFromHeader(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Authorization"))
                .filter(header -> header.startsWith("Bearer "))
                .map(header -> header.substring(7));
    }

    // "accessToken" 쿠키에서 토큰 추출
    private Optional<String> extractTokenFromCookie(HttpServletRequest request) {
        return Optional.ofNullable(request.getCookies())
                .stream()
                .flatMap(Arrays::stream)
                .filter(cookie -> "accessToken".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
}
