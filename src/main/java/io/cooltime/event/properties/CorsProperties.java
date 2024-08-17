package io.cooltime.event.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "application.cors")
public class CorsProperties {

    private List<String> allowedOrigin;
}
