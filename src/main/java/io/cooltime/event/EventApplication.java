package io.cooltime.event;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication(exclude = {
	    HibernateJpaAutoConfiguration.class
})
@ConfigurationPropertiesScan
public class EventApplication {

	public static void main(String[] args) {
		System.setProperty("reactor.netty.http.server.accessLogEnabled", "true");

		ApplicationContext applicationContext = SpringApplication.run(EventApplication.class, args);

		checkProperties(applicationContext.getEnvironment());
	}

	private static void checkProperties(Environment environment) {
		log.info("[SERVER] ID: [{}]!", environment.getProperty("server.id"));
	}
}

