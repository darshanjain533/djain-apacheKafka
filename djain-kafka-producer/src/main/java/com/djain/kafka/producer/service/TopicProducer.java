package com.djain.kafka.producer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopicProducer {
	
	private final KafkaTemplate<String, String> kafkatemplate;
	
	@Value("${topic.name.producer}")
    private String topicName;
	
	public void send(String msg) {
		log.info("Inside Producer Send service");
		kafkatemplate.send(topicName, msg);
	}
}
