package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface transformableInterface {
    public <T> T as(Class<T> responseType) throws JsonProcessingException;
}
