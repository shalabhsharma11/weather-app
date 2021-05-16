package com.assignment.spring.controller;

import com.assignment.spring.api.WeatherResponse;
import com.assignment.spring.confgiuration.WeatherConfig;
import com.assignment.spring.constant.Constants;
import com.assignment.spring.entity.WeatherEntity;
import com.assignment.spring.service.WeatherService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class WeatherController {

    private WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @RequestMapping("/weather")
    public WeatherEntity weather(@RequestParam String city) {
        log.info("Requesting weather information for city: {}", city);
        return weatherService.getCurrentWeatherInformation(city);
    }
}
