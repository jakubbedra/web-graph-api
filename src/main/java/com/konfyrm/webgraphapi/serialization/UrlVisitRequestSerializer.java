package com.konfyrm.webgraphapi.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.konfyrm.webgraphapi.domain.message.UrlVisitRequest;
import org.apache.kafka.common.serialization.Serializer;

public class UrlVisitRequestSerializer implements Serializer<UrlVisitRequest> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String s, UrlVisitRequest urlVisitRequest) {
        try {
            return objectMapper.writeValueAsBytes(urlVisitRequest);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing UrlVisitRequest", e);
        }
    }

}

