package gguip1.community.global.filter;

import gguip1.community.global.context.SecurityContext;
import gguip1.community.global.exception.ErrorCode;
import gguip1.community.global.exception.ErrorException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Arrays;

/**
 * 세션 기반 인증 필터
 * OncePerRequestFilter를 상속하여 각 요청마다 한 번씩 실행되는 필터
 * 특정 경로는 필터링에서 제외
 * 세션에서 userId를 추출하여 SecurityContext에 설정
 * 인증 실패 시 예외 처리
 * 요청 처리 후 SecurityContext 정리
 */
@Component
@RequiredArgsConstructor
public class SessionAuthFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;

    /*
     * 필터링 제외 경로 목록
     * 로그인 회원가입, 이메일/닉네임 중복 확인, 약관, 에러 처리 등
     */
    private static final String[] EXCLUDED_PATHS = {
            "/auth", // 로그인 및 로그아웃 엔드포인트
            "/users", // 회원가입 엔드포인트
            "/users/check-email", // 이메일 중복 확인 엔드포인트
            "/users/check-nickname", // 닉네임 중복 확인 엔드포인트
            "/terms", // 이용약관
            "/privacy", // 개인정보처리방침
            "/error", // 에러 처리 엔드포인트
            "/images/profile-img" // 프로필 이미지 업로드 엔드포인트
    };

    /*
     * 필터링 제외 경로 설정
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return Arrays.asList(EXCLUDED_PATHS).contains(path);
    }

    /*
     * 세션 기반 인증 처리
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            HttpSession httpSession = request.getSession(false); // 기존 세션이 없으면 null 반환

            if (httpSession == null) {
                throw new ErrorException(ErrorCode.UNAUTHORIZED);
            } // 세션이 없으면 인증 실패

            Long userId = (Long) httpSession.getAttribute("userId"); // 세션에서 userId 추출

            if (userId == null) {
                throw new ErrorException(ErrorCode.UNAUTHORIZED);
            } // userId가 없으면 인증 실패

            SecurityContext.setCurrentUserId(userId); // SecurityContext에 userId 설정

            filterChain.doFilter(request, response); // 다음 필터 또는 리소스로 요청 전달
        } catch (ErrorException ex) {
            handlerExceptionResolver.resolveException(request, response, null, ex);
        } catch (Exception ex) {
            handlerExceptionResolver.resolveException(request, response, null, new ErrorException(ErrorCode.INTERNAL_SERVER_ERROR));
        } finally {
            SecurityContext.clear(); // 요청 처리 후 SecurityContext 정리
        }
    }
}
