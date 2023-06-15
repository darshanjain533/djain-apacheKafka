package com.djain.kafka.producer.config;

import java.time.Duration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.security.oauthbearer.secured.ValidateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListenerConfigurer;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@EnableKafka
public class KafkaConfiguration implements KafkaListenerConfigurer {

	public static final String ORDERS = "data";
	

	@Value("${app.kafka.deadletter.retention}")
	@NotNull @Valid
	private Duration retention;
	
	@Value("${app.kafka.deadletter.suffix}")
	@NotNull @Valid
	private String suffix;
	
	@Value("${app.kafka.backoff.initial-interval}")
	@NotNull @Valid
	private Duration initialInterval;	
	
	@Value("${app.kafka.backoff.max-interval}")
	@NotNull @Valid @Positive
	private Duration maxInterval;
	
	@Value("${app.kafka.backoff.max-retries}")
	@NotNull @Valid
	private int retries;
	
	@Value("${app.kafka.backoff.multiplier}")
	@NotNull @Valid @Positive
	private double multiplier;
	
	
	@Bean
	public NewTopic ordersTopic() {
		return TopicBuilder.name(ORDERS)
				// Use more than one partition for frequently used input topic
				.partitions(6)
				.build();
	}

	@Bean
	public NewTopic deadLetterTopic() {
		// https://docs.spring.io/spring-kafka/docs/2.8.1/reference/html/#configuring-topics
		return TopicBuilder.name(ORDERS + retention)
				// Use only one partition for infrequently used Dead Letter Topic
				.partitions(1)
				// Use longer retention for Dead Letter Topic, allowing for more time to troubleshoot
				.config(TopicConfig.RETENTION_MS_CONFIG, "" + retention.toMillis())
				.build();
	}

	@Bean
	public DefaultErrorHandler defaultErrorHandler(	KafkaOperations<Object, Object> operations) {
		// Publish to dead letter topic any messages dropped after retries with back off
		var recoverer = new DeadLetterPublishingRecoverer(operations,
				// Always send to first/only partition of DLT suffixed topic
				(cr, e) -> new TopicPartition(cr.topic() + suffix, 0));

		// Spread out attempts over time, taking a little longer between each attempt
		// Set a max for retries below max.poll.interval.ms; default: 5m, as otherwise we trigger a consumer rebalance
		var exponentialBackOff = new ExponentialBackOffWithMaxRetries(retries);
		exponentialBackOff.setInitialInterval(initialInterval.toMillis());
		exponentialBackOff.setMultiplier(multiplier);
		exponentialBackOff.setMaxInterval(maxInterval.toMillis());

		// Do not try to recover from validation exceptions when validation of orders failed
		var errorHandler = new DefaultErrorHandler(recoverer, exponentialBackOff);
		errorHandler.addNotRetryableExceptions(ValidateException.class);

		return errorHandler;
	}

	@Autowired
	private LocalValidatorFactoryBean validator;

	@Override
	public void configureKafkaListeners(KafkaListenerEndpointRegistrar registrar) {
		// https://docs.spring.io/spring-kafka/docs/2.8.1/reference/html/#kafka-validation
		registrar.setValidator(this.validator);
	}
}
