package com.konfyrm.webgraphapi.maanger;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class JsonFileManagerImpl implements JsonFileManager {

    private static final Logger LOGGER = LogManager.getLogger(JsonFileManagerImpl.class);

    @Value("${application.api.json-path}")
    private String filePath;

    @Override
    public <T> void exportObject(T object, String executionId) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            File file = new File(getFilePath(object.getClass(), executionId));
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            mapper.writeValue(file, object);
        } catch (IOException e) {
            LOGGER.warn("Error while exporting graph for execution " + executionId + ": " + e.getMessage());
        }
    }

    @Override
    public <T> T importObject(Class<T> clazz, String executionId) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(new File(getFilePath(clazz, executionId)), clazz);
        } catch (IOException e) {
            LOGGER.warn("Error while reading graph file for execution " + executionId + ": " + e.getMessage());
            return null;
        }
    }

    private <T> String getFilePath(Class<T> clazz, String executionId) {
        return filePath + "/" + toKebabCase(clazz.getName()) + "/" + executionId + ".json";
    }

    private String toKebabCase(String input) {
        return input.replaceAll("([a-z])([A-Z]+)", "$1-$2").toLowerCase();
    }

}
