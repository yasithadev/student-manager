package com.example.drone.util;

import com.example.drone.model.error.ValidationErrorResponse;
import com.example.drone.model.error.Violation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Java6Assertions.*;

public class ResponseBodyMatchers {
    private ObjectMapper objectMapper = new ObjectMapper();

    public <T> ResultMatcher containsObjectAsJson(Object expectedObject, Class<T> targetClass) {
        return mvcResult -> {
            String json = mvcResult.getResponse().getContentAsString();
            T actualObject = objectMapper.readValue(json, targetClass);
            assertThat(actualObject).isEqualToComparingFieldByField(expectedObject);
        };
    }
    public ResultMatcher containsError(String expectedFieldName,String expectedMessage) {
        return mvcResult -> {
            String json = mvcResult.getResponse().getContentAsString();
            ValidationErrorResponse errorResult = objectMapper.readValue(json, ValidationErrorResponse.class);
            List<Violation> fieldErrors = errorResult.getViolations().stream()
                    .filter(fieldError -> fieldError.getFieldName().equals(expectedFieldName))
                    .filter(fieldError -> fieldError.getMessage().equals(expectedMessage))
                    .collect(Collectors.toList());

            assertThat(fieldErrors)
                    .hasSize(1)
                    .withFailMessage("expecting exactly 1 error message"
                                    + "with field name '%s' and message '%s'",
                            expectedFieldName,
                            expectedMessage);
        };
    }
    public static ResponseBodyMatchers responseBody(){
        return new ResponseBodyMatchers();
    }
}
