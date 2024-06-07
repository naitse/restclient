package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.*;

@Builder
@Getter
@Setter
public class ApiResponse implements transformableInterface{
    private final Gson gson = DefaultGson.getInstance();
    private final ObjectMapper objectMapper = DefaultMapper.getInstance();
    private Integer statusCode;
    private String message;
    private String body;

    public AssertableResponse validate(){
        return AssertableResponse.builder().apiResponse(this).build();
    }

    public <T> T as(Class<T> responseType) throws JsonProcessingException {
        return objectMapper.readValue(this.body, responseType);
    }

}

