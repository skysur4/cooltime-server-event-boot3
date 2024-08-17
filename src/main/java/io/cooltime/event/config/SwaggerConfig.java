package io.cooltime.event.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.cooltime.event.properties.SwaggerProperties;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;

@Profile("local|dev")
@Configuration
@OpenAPIDefinition(info = @Info(title = "Server Event Streamer", version = "version 1.0"))
public class SwaggerConfig {
	@Autowired
	SwaggerProperties swaggerProperties;

	@Bean
	OpenAPI openAPI() {
		return swaggerProperties.getApiInfoV1();
	}

	@Bean
	GroupedOpenApi publish() {
		return GroupedOpenApi.builder()
				.group("publisher")
//				.pathsToExclude("/api/v1/internal/**")
				.pathsToMatch("/api/v1/publish/**")
				.build();
	}

	@Bean
	GroupedOpenApi subscriber() {
		return GroupedOpenApi.builder()
				.group("subscriber")
				.pathsToMatch("/subscribe/**")
				.build();
	}
}
