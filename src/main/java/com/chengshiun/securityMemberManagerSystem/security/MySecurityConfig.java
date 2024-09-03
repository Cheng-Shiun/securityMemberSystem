package com.chengshiun.securityMemberManagerSystem.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class MySecurityConfig {

    //創建使用 BCrypt 演算法加密 password
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //使用 InMemory 創建使用者帳號
    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        PasswordEncoder encoder = passwordEncoder();

        UserDetails userTest1 = User
                .withUsername("admin")
                .password(encoder.encode("111"))
                .roles("ADMIN", "NORMAL_MEMBER")
                .build();

        UserDetails userTest2 = User
                .withUsername("test")
                .password(encoder.encode("222"))
                .roles("NORMAL_MEMBER")
                .build();

        return new InMemoryUserDetailsManager(userTest1, userTest2);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                //設定 Session 的創建機制
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                )

                //設定 CSRF 保護
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/member/register")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(createCsrfHandler())
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())

                //設定 api 訪問是否認證
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/member/register").permitAll()
                        .requestMatchers("/member/getMember/*").hasRole("ADMIN")
                        .requestMatchers("hello", "/member/update/*").hasAnyRole("ADMIN", "NORMAL_MEMBER", "VIP_MEMBER")
                        .anyRequest().authenticated()
                )

                //設定 CORS 保護
                .cors(cors -> cors
                        .configurationSource(createCorsConfig())
                )

                .build();
    }
    //創建一個 csrfHandler() 方法
    private CsrfTokenRequestAttributeHandler createCsrfHandler() {
        CsrfTokenRequestAttributeHandler csrfHandler = new CsrfTokenRequestAttributeHandler();
        csrfHandler.setCsrfRequestAttributeName(null);

        return csrfHandler;
    }

    //創建一個 corsConfig() 方法
    private CorsConfigurationSource createCorsConfig() {
        CorsConfiguration config = new CorsConfiguration();
        //設定請求 api 的 response header 參數 -> 表示瀏覽器發起的 preflight 請求被後端允許通過
        config.setAllowedOrigins(List.of("*"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("*"));
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
