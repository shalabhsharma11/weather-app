package com.assignment.spring.confgiuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Configuration
@ConfigurationProperties("weather")
@Data
public class WeatherConfig {

    @NotNull
    private String apiKey;
    @NotNull
    private Long requestWaitMs;
}