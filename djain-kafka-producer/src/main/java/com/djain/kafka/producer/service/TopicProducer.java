package com.djain.kafka.producer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopicProducer {
	
	private final KafkaTemplate<String, String> kafkaTemplate;
	
	@Value("${topic.name.producer}")
    private String topicName;
	
	public void send(String msg) {
		log.info("Inside Producer Send service");
		//kafkatemplate.send(topicName, msg);
		
		ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicName, msg);

		future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

			@Override
			public void onSuccess(SendResult<String, String> result) {
				System.out.println("Sent message=[" + msg + "] with offset=[" + result.getRecordMetadata().offset() + "]");
			}

			@Override
			public void onFailure(Throwable ex) {
				System.out.println("Unable to send message=[" + msg + "] due to : " + ex.getMessage());
			}
		});
	}
}


/*
* Kafka is a fast stream processing platform. Therefore, it's better to handle the results asynchronously so that the subsequent messages do not wait for the result of the previous message. We can do this through a ListenableFuture:
*
*/