package com.example.springbootweb.configuration;

import com.nimbusds.jose.JWSAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${jwt.signerKey}")
    private String SIGNER_STRING;
    private final String[] PUBLIC_POST_ENDPOINT = {
            "/user",
            "/auth/login",
            "/auth/introspect",
            "/auth/logout",
            "/auth/refresh"
    };
    private final String[] PUBLIC_GET_ENDPOINT = {
            "/book",
    };

    @Autowired
    private CustomJwtDecoder customJwtDecoder;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(
                request -> {
                    request.requestMatchers(HttpMethod.POST, PUBLIC_POST_ENDPOINT).permitAll()
                            .requestMatchers(HttpMethod.GET, PUBLIC_GET_ENDPOINT).permitAll()
                            .anyRequest().authenticated();
                }
        );

        httpSecurity.oauth2ResourceServer(
                oauth2 -> oauth2.jwt(
                        jwtConfigurer -> jwtConfigurer
                                .decoder(this.customJwtDecoder)
//                                .decoder(this.jwtDecoder())
                                .jwtAuthenticationConverter(this.jwtAuthenticationConverter())
                )
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
        );

        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(converter);
        return jwtConverter;
    }

    @Bean
    JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(
                SIGNER_STRING.getBytes(),
                JWSAlgorithm.HS512.getName()
        );
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
