package gguip1.community.global.config;


import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;
import java.util.logging.Filter;

@Configuration
public class CorsConfig {

    /**
     * CORS 필터 설정
     * setAllowCredentials(true) : 자격 증명 허용
     * setAllowedOrigins(List.of("http://localhost:3000")) : 허용할 출처 설정
     * setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")) : 허용할 HTTP 메서드 설정
     * setAllowedHeaders(List.of("*")) : 허용할 헤더 설정
     * setMaxAge(3600L) : 사전 요청 캐시 시간 설정
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterFilterRegistrationBean(CorsFilter corsFilter) {
        FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>(corsFilter);
        registrationBean.setOrder(0); // 모든 필터 중에서 가장 먼저 실행되도록 설정
        return registrationBean;
    }
}
