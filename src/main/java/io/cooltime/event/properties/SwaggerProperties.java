package io.cooltime.event.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "springdoc.api-info")
public class SwaggerProperties {
	private String title;
	private String description;
	private String version;
	private String termsOfService;
	private License license;
	private Contact contact;

	public OpenAPI getApiInfoV1() {
        String jwtSchemeName = "JWT TOKEN";
        SecurityScheme securityScheme = new SecurityScheme()
                .name(HttpHeaders.AUTHORIZATION)
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .bearerFormat("JWT")
                .scheme("Bearer");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
        Components securityComponent = new Components().addSecuritySchemes(jwtSchemeName, securityScheme);


		return new OpenAPI()
		        .info(getInfoV1())
		        .addSecurityItem(securityRequirement)
		        .components(securityComponent);
	}

	public Info getInfoV1() {
		return new Info()
	        		.title(title)
	        		.description(description)
	        		.termsOfService(termsOfService)
	        		.version("version 1")
	        		.license(license);
	}
}