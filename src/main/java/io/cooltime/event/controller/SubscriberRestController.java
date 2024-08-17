package io.cooltime.event.controller;

import java.time.Instant;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cooltime.event.EventConstants;
import io.cooltime.event.model.AlertMessage;
import io.cooltime.event.service.RedisSubscriber;
import io.swagger.v3.oas.annotations.Operation;
import reactor.core.publisher.Flux;

/**
 * Streaming console 기능
 *
 * @author chris.kim
 * @since 2022-12-08
 * @version 1.0
*/

@RestController
@RequestMapping("/subscribe")
public class SubscriberRestController {
	@Autowired
	RedisSubscriber redisSubscriber;

	@Autowired
	ObjectMapper objectMapper;

	@Operation(summary = "알림 받음", description = "모든 사용자 및 나에게 온 알림 받음")
	@GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ServerSentEvent<AlertMessage>> subscribe(Authentication authentication) {
		if(!authentication.isAuthenticated()) {
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
		}

		String userId = authentication.getName();

		if(StringUtils.isEmpty(userId)) {
			throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
		}

		return redisSubscriber.comsume(userId)
				.map(message -> {
					try {
						AlertMessage em = objectMapper.readValue(message, AlertMessage.class);

						return ServerSentEvent.<AlertMessage> builder()
								.id(String.valueOf(Instant.now().toEpochMilli()))
								.event(em.getType())
								.data(em)
								.build();
					} catch (JsonProcessingException e) {
						return ServerSentEvent.<AlertMessage> builder()
								.id(String.valueOf(Instant.now().toEpochMilli()))
								.event(EventConstants.NOTIFY_ERROR)
								.data(new AlertMessage(e.getMessage()))
								.build();
					}
				});
	}

	@GetMapping(value = "/tester", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ServerSentEvent<AlertMessage>> subscribeTest() {
		return redisSubscriber.comsume("tester")
				.map(message -> {
					try {
						AlertMessage em = objectMapper.readValue(message, AlertMessage.class);

						return ServerSentEvent.<AlertMessage> builder()
								.id(String.valueOf(Instant.now().toEpochMilli()))
								.event(em.getType())
								.data(em)
								.build();
					} catch (JsonProcessingException e) {
						return ServerSentEvent.<AlertMessage> builder()
								.id(String.valueOf(Instant.now().toEpochMilli()))
								.event(EventConstants.NOTIFY_ERROR)
								.data(new AlertMessage(e.getMessage()))
								.build();
					}
				});

	}
}