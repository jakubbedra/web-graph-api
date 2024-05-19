package com.konfyrm.webgraphapi.maanger;

public interface JsonFileManager {
    <T> void exportObject(T object, String executionId);
    <T> T importObject(Class<T> clazz, String executionId);
}