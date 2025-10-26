package gguip1.community.global.config;

import gguip1.community.global.exception.ErrorCode;
import gguip1.community.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final String[] PUBLIC_LIST = {
            "/users",
            "/auth",
            "/images/profile-img",
            "/images/post-img",
            "/images",
            "/privacy/**",
            "/terms/**",
            "/css/**",
            "/js/**",
            "/assets/**",
            "/components/**",
            "/static/**",
            "/users/check-email",
            "/users/check-nickname",
            "/error",
    };

    private final HandlerExceptionResolver handlerExceptionResolver;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(PUBLIC_LIST).permitAll()
                    .anyRequest().authenticated()
            )
            .httpBasic(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .maximumSessions(3)
                    .maxSessionsPreventsLogin(false) // true면 중복 로그인 차단, false면 기존 세션 만료
                    .expiredSessionStrategy(event -> handlerExceptionResolver.resolveException(
                            event.getRequest(),
                            event.getResponse(),
                            null,
                            new ErrorException(ErrorCode.SESSION_EXPIRED)
                    ))
            )
            .exceptionHandling(ex -> ex
                    .authenticationEntryPoint((request, response, authException) -> {
                        handlerExceptionResolver.resolveException(
                                request,
                                response,
                                null,
                                new ErrorException(ErrorCode.INVALID_CREDENTIALS)
                        );
                    })
                    .accessDeniedHandler(((request, response, accessDeniedException) -> {
                        handlerExceptionResolver.resolveException(
                                request,
                                response,
                                null,
                                new ErrorException(ErrorCode.ACCESS_DENIED)
                        );
                    }))
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        /*
         * 비밀번호 암호화 설정
         * - BCryptPasswordEncoder 사용
         * - 비밀번호를 안전하게 저장하기 위해 해시 함수 적용
         */
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        /*
         * AuthenticationManager 빈 등록
         */
        return config.getAuthenticationManager();
    }
}
