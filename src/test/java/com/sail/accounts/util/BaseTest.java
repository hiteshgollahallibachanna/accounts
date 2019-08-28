package com.sail.accounts.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.io.Resources;
import com.sail.accounts.commons.model.ErrorResponse;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class BaseTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public BaseTest() {
        JavaTimeModule module = new JavaTimeModule();
        objectMapper.registerModules(module);

    }

    protected ErrorResponse loadErrorResponse(String fileName) throws IOException {
        return objectMapper.readValue(new File("src/test/resources/" + fileName + ""), new TypeReference<ErrorResponse>() {
        });
    }

    protected String getJsonData(String fileName) throws IOException {
        URL url = Resources.getResource(fileName);
        return Resources.toString(url, StandardCharsets.UTF_8);
    }
}
