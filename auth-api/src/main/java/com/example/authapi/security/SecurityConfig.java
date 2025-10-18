package com.example.authapi.security;


import com.example.authapi.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.Filter;


@Configuration
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain chain(HttpSecurity http, JwtService jwt, UserRepository users) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(reg->reg.requestMatchers("/api/auth/**","/actuator/health")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .addFilterBefore(new JwtFilter(jwt, users), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    public static class  JwtFilter implements Filter{
        private final JwtService jwt;
        private final UserRepository users;

        public JwtFilter(JwtService jwt, UserRepository users) {
            this.jwt = jwt;
            this.users = users;
        }

        @Override
        public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException{

            var r = (HttpServletRequest)req;
            var h = r.getHeader("Authorization");

            if(h != null && h.startsWith("Bearer ")) {
                try{
                    var email = jwt.validateAndGetSubject(h.substring(7));
                    if(users.findByEmail(email).isPresent()){
                        SecurityContextHolder.getContext().setAuthentication(
                                new UsernamePasswordAuthenticationToken(email, null, List.of())
                        );
                    }
                }catch (Exception ignored){}
            }
            chain.doFilter(req, res);
        }
    }
}
