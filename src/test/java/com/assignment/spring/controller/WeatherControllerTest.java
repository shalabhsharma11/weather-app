package com.assignment.spring.controller;

import com.assignment.spring.entity.WeatherEntity;
import com.assignment.spring.service.WeatherService;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//testing compare
@ExtendWith(SpringExtension.class)
@WebMvcTest(WeatherController.class)
class WeatherControllerTest {

    public static String CITY = "amsterdam";

    @MockBean
    WeatherService weatherService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testWeather() throws Exception {

        WeatherEntity entity = new WeatherEntity();
        entity.setCity(CITY);
        entity.setCountry("NL");
        entity.setDateTime(System.currentTimeMillis());
        entity.setTemperature(200.00);

        Mockito.when(weatherService.getCurrentWeatherInformation(CITY)).thenReturn(entity);

        mockMvc.perform(get("/weather?city="+CITY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['city']", Matchers.is(CITY)));
    }

    @Test
    public void testWeatherBadRequest() throws Exception {

        WeatherEntity entity = new WeatherEntity();
        entity.setCity(CITY);
        entity.setCountry("NL");
        entity.setDateTime(System.currentTimeMillis());
        entity.setTemperature(200.00);

        Mockito.when(weatherService.getCurrentWeatherInformation(CITY)).thenReturn(entity);

        mockMvc.perform(get("/weather"))
                .andExpect(status().isBadRequest());
    }
}