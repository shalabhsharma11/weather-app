package com.assignment.spring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
public class WeatherApiException {

    @Getter
    private final long timestamp;
    @Getter
    private String message;
    @Getter
    private HttpStatus httpStatus;
}
