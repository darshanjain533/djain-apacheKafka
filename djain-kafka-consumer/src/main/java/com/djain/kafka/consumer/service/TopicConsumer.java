package com.djain.kafka.consumer.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class TopicConsumer {
	
	@Value("${topic.name.consumer")
    private String topicName;
	
	public void consume(ConsumerRecord<String, String> payload) {
		log.info("Topic is: {}", topicName);
        log.info("key is: {}", payload.key());
        log.info("Headers are: {}", payload.headers());
        log.info("Partion is: {}", payload.partition());
        log.info("Order is: {}", payload.value());
	}
}
