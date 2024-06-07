package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import lombok.Builder;
import org.apache.http.util.Asserts;

@Builder
public class AssertableResponse implements transformableInterface{
    private ApiResponse apiResponse;

    @Override
    public <T> T as(Class<T> responseType) throws JsonProcessingException {
        return apiResponse.as(responseType);
    }

    public AssertableResponse code(int expectedCode) {
        Asserts.check(apiResponse.getStatusCode() == expectedCode, "Incorrect response code");
        return this;
    }

    public AssertableResponse body(String jsonPath, Object epectedValue) {

        Object pathElement = getPathElement(jsonPath);
        assert pathElement.equals(epectedValue);
        return this;
    }

    public AssertableResponse body(String jsonPath, Condition condition) {
        try {
            Object pathElement = getPathElement(jsonPath);
        } catch (AssertionError e) {
            if(condition == Condition.NOT_NULL){
                throw e;
            }
        }
        return this;
    }

    private Object getPathElement(String jsonPath){
        DocumentContext documentContext = JsonPath.parse(apiResponse.getBody());
        try {
            return documentContext.read(jsonPath);
        } catch (PathNotFoundException e) {
            throw new AssertionError(e.getMessage());
        }
    }

}
