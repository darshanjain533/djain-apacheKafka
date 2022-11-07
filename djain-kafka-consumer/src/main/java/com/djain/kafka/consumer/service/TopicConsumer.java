package com.djain.kafka.consumer.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class TopicConsumer {
	
	@Value("${topic.name.consumer}")
    private String topicName;
	
	@KafkaListener(topics = "#{'${topic.name.consumer}'}", groupId = "#{'${spring.kafka.consumer.group-id}'}")
	public void consume(ConsumerRecord<String, String> payload) {
		log.info("Topic is: {}", topicName);
        log.info("key is: {}", payload.key());
        log.info("Headers are: {}", payload.headers());
        log.info("Partion is: {}", payload.partition());
        log.info("Order is: {}", payload.value());
	}
}


/*
	For a topic with multiple partitions, however, a @KafkaListener can explicitly subscribe to a particular partition of a topic with an initial offset
	 @KafkaListener(
		  topicPartitions = @TopicPartition(topic = "topicName",
		  partitionOffsets = {
		    @PartitionOffset(partition = "0", initialOffset = "0"), 
		    @PartitionOffset(partition = "3", initialOffset = "0")}),
		  	containerFactory = "partitionsKafkaListenerContainerFactor"
		 )
 */



/*
	We can configure listeners to consume specific message content by adding a custom filter. This can be done by setting a RecordFilterStrategy to the KafkaListenerContainerFactory:
	
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> CustomKafkaFilter() {
	
	    ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
	    factory.setConsumerFactory(consumerFactory());
	    factory.setRecordFilterStrategy(
	      record -> record.value().contains("World"));
	    
	    return factory;
	}

	We can then configure a listener to use this container factory:
	
	@KafkaListener(
	  topics = "topicName", 
	  containerFactory = "CustomKafkaFilter")
	public void listenWithFilter(String message) {
	    System.out.println("Received Message in filtered listener: " + message);
	}

*/




