package com.assignment.spring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.UUID;

import javax.servlet.ServletException;

@RestControllerAdvice
public class WeatherExceptionHandler {


    @ExceptionHandler(value = HttpClientErrorException.class)
    public ResponseEntity<Object> handleHttpClientErrorException(RuntimeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.valueOf(((HttpClientErrorException) exception).getStatusCode().name()));
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public WeatherApiException handleInternalServerError(Exception exception) {
        return getWeatherApiException(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = ServletException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public WeatherApiException handleBadRequest(Exception exception) {
        return getWeatherApiException(exception, HttpStatus.BAD_REQUEST);
    }


    private WeatherApiException getWeatherApiException(Exception exception, HttpStatus status) {
        return WeatherApiException.builder()
                .message(exception.getMessage())
                .httpStatus(status)
                .timestamp(System.currentTimeMillis())
                .build();
    }

}
