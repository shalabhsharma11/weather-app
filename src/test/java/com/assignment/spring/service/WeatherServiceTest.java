package com.assignment.spring.service;

import com.assignment.spring.api.Main;
import com.assignment.spring.api.Sys;
import com.assignment.spring.api.Weather;
import com.assignment.spring.api.WeatherResponse;
import com.assignment.spring.confgiuration.WeatherConfig;
import com.assignment.spring.constant.Constants;
import com.assignment.spring.entity.WeatherEntity;
import com.assignment.spring.repository.WeatherRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    public static String CITY = "amsterdam";
    public static String APIKEY = "835da2de57d78ef1994f2";
    public static Long REQUEST_WAIT_MS = 120000l;
    public static String COUNTRY = "NL";
    public static Double TEMP = 200.00;
    public static Long DATE = 111111l;


    WeatherService weatherService;
    WeatherEntity entity;

    @Mock
    WeatherConfig config;
    @Mock
    WeatherRepository weatherRepository;
    @Mock
    RestTemplate restTemplate;

    @BeforeEach
    void setup(){
        weatherService = new WeatherService(restTemplate, weatherRepository, config);
        entity = new WeatherEntity();
        entity.setId(1);
        entity.setCity(CITY);
        entity.setCountry(COUNTRY);
        entity.setDateTime(DATE);
        entity.setTemperature(TEMP);

        when(config.getApiKey()).thenReturn(APIKEY);
        when(config.getRequestWaitMs()).thenReturn(REQUEST_WAIT_MS);

        when(weatherRepository.save(any())).thenReturn(entity);

        String url = Constants.WEATHER_API_URL.replace("{city}", CITY).replace("{appid}", config.getApiKey());
        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.setName(CITY);
        weatherResponse.setSys(Sys.builder().country(COUNTRY).build());
        weatherResponse.setMain(Main.builder().temp(TEMP).build());
        weatherResponse.setDt(DATE);
        when(restTemplate.getForEntity(url, WeatherResponse.class)).thenReturn(new ResponseEntity<WeatherResponse>(weatherResponse ,HttpStatus.OK));


    }

    @Test
    public void testGetCurrentWeatherInformation(){

        WeatherEntity weatherEntity = weatherService.getCurrentWeatherInformation(CITY);
        assertEquals(CITY, weatherEntity.getCity());
        assertEquals(COUNTRY, weatherEntity.getCountry());
        assertEquals(TEMP, weatherEntity.getTemperature());
    }

}