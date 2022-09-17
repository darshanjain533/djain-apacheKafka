package com.djain.kafka.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class DjainKafkaConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DjainKafkaConsumerApplication.class, args);
	}

}
