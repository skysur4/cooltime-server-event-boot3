package io.cooltime.event.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cooltime.event.controller.base.BaseV1Controller;
import io.cooltime.event.model.EventMessageRequest;
import io.cooltime.event.service.RedisPublisher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;

/**
 * Streaming internal 기능
 *
 * @author chris.kim
 * @since 2022-12-08
 * @version 1.0
*/

@Slf4j
@RestController
@RequestMapping("/publish")
public class PublisherRestController extends BaseV1Controller {
	@Autowired
	RedisPublisher redisPublisher;

	@Autowired
	ObjectMapper objectMapper;

	@Operation(summary = "알림 생성", description = "특정 사용자 대상 알림 생성")
    @PostMapping
    public EventMessageRequest publish(@Parameter(hidden = true) Authentication authentication,
    		@RequestBody EventMessageRequest eventMessageRequest) throws JsonProcessingException {
		if(!authentication.isAuthenticated()) {
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
		}

//		Jwt test = (Jwt) authentication.getPrincipal();

		String userId = authentication.getName();

		if(StringUtils.isEmpty(userId)) {
			throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
		}

		// 보낸이 설정
		eventMessageRequest.setSender(userId);

		for(String user : eventMessageRequest.getReceivers()) {
			// 메시지 전송
			redisPublisher.produceMessage(user, objectMapper.writeValueAsString(eventMessageRequest.getAlertMessage()));
		}

    	return eventMessageRequest;
    }

	@Operation(summary = "알림 생성", description = "모든 사용자 대상 알림 생성")
    @PostMapping("/all")
    public EventMessageRequest publishAll(@Parameter(hidden = true) Authentication authentication,
    		@RequestBody EventMessageRequest eventMessageRequest) throws JsonProcessingException {
		if(!authentication.isAuthenticated()) {
			throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
		}

		String userId = authentication.getName();

		log.info("{} published global notification: {}", userId, objectMapper.writeValueAsString(eventMessageRequest));

		redisPublisher.produceMessage(objectMapper.writeValueAsString(eventMessageRequest.getAlertMessage()));

    	return eventMessageRequest;
    }

	@Operation(summary = "알림 생성 테스트", description = "tester 대상 알림 생성")
    @PostMapping("/test/tester")
    public EventMessageRequest publishTest(@RequestBody EventMessageRequest eventMessageRequest) throws JsonProcessingException {

    	eventMessageRequest.setSender("tester");

		redisPublisher.produceMessage("tester", objectMapper.writeValueAsString(eventMessageRequest.getAlertMessage()));

    	return eventMessageRequest;
    }

	@Operation(summary = "알림 생성 테스트", description = "모든 사용자 대상 알림 생성")
    @PostMapping("/test/all")
    public String publishAll(@RequestBody String message) {

    	redisPublisher.produceMessage(message);

    	return message;
    }
}
