package gguip1.community.global.config;

import gguip1.community.global.filter.JwtAuthFilter;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WebFilterConfig {
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public FilterRegistrationBean<Filter> jwtFilter() {
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>(); // 필터 등록 빈 생성
        filterFilterRegistrationBean.setFilter(jwtAuthFilter); // JwtAuthFilter 설정
        filterFilterRegistrationBean.addUrlPatterns("/*"); // 모든 URL 패턴에 대해 필터 적용
        filterFilterRegistrationBean.setOrder(1); // Cors Filter 이후에 적용되도록 설정
        return filterFilterRegistrationBean; // 필터 등록 빈 반환
    }
}
