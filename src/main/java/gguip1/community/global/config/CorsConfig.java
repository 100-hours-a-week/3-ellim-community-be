package gguip1.community.global.config;


import org.springframework.beans.factory.annotation.Value;
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

//    @Value("${cors.allow-credentials}")
//    private Boolean allowCredentials;
//
//    @Value("#{'${cors.allowed-origins}'.split(',')}.![trim()]}")
//    private List<String> allowedOrigins;
//
//    @Value("#{'${cors.allowed-methods}'.split(',')}.![trim()]}")
//    private List<String> allowedMethods;
//
//    @Value("#{'${cors.allowed-headers}'.split(',')}.![trim()]}")
//    private List<String> allowedHeaders;
//
//    @Value("${cors.max-age}")
//    private Long maxAge;

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(allowCredentials);
//        config.setAllowedOrigins(allowedOrigins);
//        config.setAllowedMethods(allowedMethods);
//        config.setAllowedHeaders(allowedHeaders);
//        config.setMaxAge(maxAge);
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:3000", "https://wepick.cloud"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
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
