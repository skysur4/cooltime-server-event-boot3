package io.cooltime.event.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.stereotype.Service;

import io.cooltime.event.EventConstants;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class RedisPublisher {
	@Autowired
	private ReactiveRedisOperations<String, String> redisOperations;

    public void produceMessage(String message) {
    	produceMessage(EventConstants.NOTIFY_DEFAULT_CHANNEL, message);
    }

    public void produceMessage(String channel, String message) {
    	Topic topic;
    	if(StringUtils.isEmpty(channel)) {
    		topic = new ChannelTopic(EventConstants.NOTIFICATION_QUEUE_PREFIX + EventConstants.NOTIFY_DEFAULT_CHANNEL);
    	} else {
    		topic = new ChannelTopic(EventConstants.NOTIFICATION_QUEUE_PREFIX + channel);
    	}

    	log.info("Producing {}:{}", topic.getTopic(), message);
    	produce(topic, message).subscribe(t -> log.info("Redis published for {} subscribers", t));
    }

    private Mono<Long> produce(Topic topic, String message) {
		return redisOperations.convertAndSend(topic.getTopic(), message);
    }
}