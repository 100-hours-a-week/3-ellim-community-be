package gguip1.community.global.config;


import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;
import java.util.logging.Filter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration(); // CORS 설정 객체 생성
        config.setAllowCredentials(true); // 자격 증명 허용
        config.setAllowedOrigins(List.of("http://localhost:3000")); // 허용할 출처 설정
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")); // 허용할 HTTP 메서드 설정
        config.setAllowedHeaders(List.of("*")); // 허용할 헤더 설정
        config.setMaxAge(3600L); // 사전 요청 캐시 시간 설정

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); // CORS 설정 소스 생성
        source.registerCorsConfiguration("/**", config); // 모든 경로에 대해 CORS 설정 적용
        return new CorsFilter(source); // CorsFilter 생성 및 반환
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterFilterRegistrationBean(CorsFilter corsFilter) {
        FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>(corsFilter); // CorsFilter를 필터로 등록
        registrationBean.setOrder(0); // 모든 필터 중에서 가장 먼저 실행되도록 설정
        return registrationBean;
    }
}
