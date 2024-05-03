package com.konfyrm.webgraphapi.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static com.konfyrm.webgraphapi.domain.KafkaTopicConstants.REQUEST_TOPIC;
import static com.konfyrm.webgraphapi.domain.KafkaTopicConstants.RESULT_TOPIC;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic webGraphRequestTopic() {
        return TopicBuilder.name(REQUEST_TOPIC)
                .build();
    }

    @Bean
    public NewTopic webGraphResultTopic() {
        return TopicBuilder.name(RESULT_TOPIC)
                .build();
    }

}
