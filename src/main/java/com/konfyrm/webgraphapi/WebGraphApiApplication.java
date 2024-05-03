package com.konfyrm.webgraphapi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

import static com.konfyrm.webgraphapi.domain.KafkaTopicConstants.REQUEST_TOPIC;

@SpringBootApplication
public class WebGraphApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebGraphApiApplication.class, args);
	}

}
