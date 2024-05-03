package com.konfyrm.webgraphapi.serialization;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.konfyrm.webgraphapi.domain.message.UrlVisitResult;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

public class UrlVisitResultDeserializer implements Deserializer<UrlVisitResult> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public UrlVisitResult deserialize(String s, byte[] data) {
        try {
            return objectMapper.readValue(data, UrlVisitResult.class);
        } catch (IOException e) {
            throw new RuntimeException("Error deserializing UrlVisitResult: " + e.getMessage(), e);
        }
    }

}