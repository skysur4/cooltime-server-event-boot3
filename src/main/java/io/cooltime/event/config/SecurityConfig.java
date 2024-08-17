package io.cooltime.event.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import io.cooltime.event.properties.AccessProperties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
	@Autowired
	private AccessProperties accessProperties;

	@Bean
	ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
		return new ReactiveJwtAuthenticationConverter();
	};

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		http.csrf(csrf -> csrf.disable());
		http.formLogin(login -> login.disable());
        http.httpBasic(httpBasic -> httpBasic.disable());
        http.anonymous(anonymous -> anonymous.disable());

        //disable session
        http.securityContextRepository(NoOpServerSecurityContextRepository.getInstance());

        /**
         * 인증 처리 전 필터 추가
         */
//        http.addFilterBefore(필터, OAuth2LoginAuthenticationFilter.class);

		String[] freePaths = accessProperties.getFreePaths();
    	log.info("### Security freePaths patterns: " + Arrays.toString(freePaths));

    	/**
    	 * 1. 권한 없이 접속 가능
    	 */
		http.authorizeExchange(requests -> {
			requests.pathMatchers(freePaths).permitAll();
		});

    	/**
    	 * 2. 그 외에는 인증 받은 요청만 허용
    	 */
		http.authorizeExchange(exchange -> {
			exchange.anyExchange().authenticated();
		});

		http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

		/**
         * 인증 처리 후 필터 추가
         */
        //http.addFilterAfter(필터, SecurityWebFiltersOrder.AUTHORIZATION);

        final SecurityWebFilterChain build = http.build();

        return build;
    }
}