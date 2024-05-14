package com.konfyrm.webgraphapi.service.impl;

import com.konfyrm.webgraphapi.domain.message.UrlVisitRequest;
import com.konfyrm.webgraphapi.service.UrlVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.konfyrm.webgraphapi.domain.KafkaTopicConstants.REQUEST_TOPIC;

@Service
public class UrlVisitServiceImpl implements UrlVisitService {

    private final KafkaTemplate<String, UrlVisitRequest> kafkaTemplate;

    @Autowired
    public UrlVisitServiceImpl(
            KafkaTemplate<String, UrlVisitRequest> kafkaTemplate
    ) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendUrlVisitRequest(String executionUuid, String startingUrl) {
        UrlVisitRequest request = UrlVisitRequest.builder()
                .executionUuid(executionUuid)
                .url(startingUrl)
                .build();
        kafkaTemplate.send(REQUEST_TOPIC, request);
    }

}