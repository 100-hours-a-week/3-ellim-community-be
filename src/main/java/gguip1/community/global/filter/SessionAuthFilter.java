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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
@Slf4j
public class SessionAuthFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Value("#{'${auth.excluded-paths}'.split(',')}")
    private final String[] EXCLUDED_PATHS;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return Arrays.asList(EXCLUDED_PATHS).contains(path);
    }

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
        } finally {
            SecurityContext.clear(); // 요청 처리 후 SecurityContext 정리
        }
    }
}
