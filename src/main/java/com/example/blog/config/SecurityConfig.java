package com.example.blog.config;

import com.example.blog.security.CustomUserDetailsService;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.List;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

    @Bean
    public SecretKey jwtSecretKey(@Value("${app.jwt.secret}") String secret){
        byte[] bytes = Base64.getDecoder().decode(secret);
        if(bytes.length < 32){
            throw new IllegalArgumentException("JWT secret must be at least 32 bytes");
        }
        return new SecretKeySpec(bytes, "HmacSHA256");
    }
    @Bean
    public JwtEncoder jwtEncoder(SecretKey key){
        return NimbusJwtEncoder.withSecretKey(key).build();
    }
    @Bean
    public JwtDecoder jwtDecoder(
            SecretKey key,
            @Value("${app.jwt.issuer}") String issuer
    ){
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
        decoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(issuer));
        return decoder;
    }
    @Bean
    public Converter<Jwt, JwtAuthenticationToken> jwtAuthenticationConverter(){
        return jwt -> {
            String role = jwt.getClaimAsString("role");
            List<GrantedAuthority> authorities = switch (role == null ? "": role){
                case "USER", "ADMIN" -> List.of(new SimpleGrantedAuthority("ROLE_"+role));
                default -> List.of();
            };
            return new JwtAuthenticationToken(jwt, authorities, jwt.getSubject());
        };
    }
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            Converter<Jwt, JwtAuthenticationToken> converter) {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/register").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/hello")
                        .permitAll()
                        .requestMatchers("/api/auth/me")
                        .authenticated()
                        .requestMatchers("/api/users", "/api/users/**")
                        .hasRole("ADMIN")
                        .anyRequest().denyAll()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(converter)
                        )
                );

        return http.build();
    }
}
