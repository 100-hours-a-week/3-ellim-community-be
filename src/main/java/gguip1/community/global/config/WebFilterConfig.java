package gguip1.community.global.config;

import gguip1.community.global.filter.SessionAuthFilter;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WebFilterConfig {
    private final SessionAuthFilter sessionAuthFilter;

    /**
     * 세션 기반 인증 필터 적용
     * setFilter : 적용할 필터 설정
     * addUrlPatterns : 필터를 적용할 URL 패턴 설정
     * setOrder : 필터의 우선순위 설정 (낮을수록 우선순위 높음)
     * Cors Filter 이후에 적용되도록 설정
     */
    @Bean
    public FilterRegistrationBean<Filter> sessionFilter() {
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(sessionAuthFilter);
        filterFilterRegistrationBean.addUrlPatterns("/*");
        filterFilterRegistrationBean.setOrder(1);
        return filterFilterRegistrationBean;
    }
}
