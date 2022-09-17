package com.djain.kafka.producer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.djain.kafka.producer.service.TopicProducer;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/kafka/producer")
public class ProducerController {
	 private final TopicProducer topicProducer;
	    @GetMapping(value = "/send")
	    public void send(){
	    	log.info("Inside Producer. Streaming services from Producer.");
	        topicProducer.send("Sending Message from Producer");
	    }
}