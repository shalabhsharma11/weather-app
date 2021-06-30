package com.assignment.spring.service;

import com.assignment.spring.api.WeatherResponse;
import com.assignment.spring.confgiuration.WeatherConfig;
import com.assignment.spring.constant.Constants;
import com.assignment.spring.entity.WeatherEntity;
import com.assignment.spring.repository.WeatherRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WeatherService {

    private WeatherRepository weatherRepository;
    private RestTemplate restTemplate;
    private WeatherConfig weatherConfig;
    private Map<String, WeatherEntity> cityWeatherCacheMap = new ConcurrentHashMap<>();


    /**
     * Autowired Constructor
     *
     * @param restTemplate
     * @param weatherRepository
     * @param weatherConfig
     */
    public WeatherService(RestTemplate restTemplate, WeatherRepository weatherRepository, WeatherConfig weatherConfig) {
        this.restTemplate = restTemplate;
        this.weatherConfig = weatherConfig;
        this.weatherRepository = weatherRepository;
    }

    /**
     * Persist weather entity
     * @param weather
     * @return
     */
    public WeatherEntity save(WeatherEntity weather) {
        return weatherRepository.save(weather);
    }

    /**
     * Get city current weather info
     */
    public WeatherEntity getCurrentWeatherInformation(String city) {

        // Limit the no. of requests to Weather API. If request is not older than requestWaitMS, return weather info from cache.
        if(!makeCallToWeatherApi(city)){
            log.info("Not calling api for weather information and getting it from cache, as request is early.");
            return getCachedWeatherEntity(city);
        }

        String url = Constants.WEATHER_API_URL.replace("{city}", city).replace("{appid}", weatherConfig.getApiKey());
        ResponseEntity<WeatherResponse> response = null;
        Long callTimeMs = System.currentTimeMillis();
        log.debug("Requesting weather url: {}", url);
        try {
            response = restTemplate.getForEntity(url, WeatherResponse.class);

        } catch (Exception e) {
            log.error("Error requesting api {}; Reason: {}", url, e.getMessage(), e);
            throw e;
        }

        return mapper(city, callTimeMs, response.getBody());
    }


    /**
     * Persist weather info to db if not present in cache
     * @param city
     * @param response
     * @return
     */
    private WeatherEntity mapper(String city, Long callTimeMs, WeatherResponse response) {
        try {

            WeatherEntity entity = getCachedWeatherEntity(city);

            // If temperature is same, return from cache and don't persist in DB.
            if (entity != null && entity.getTemperature().equals(response.getMain().getTemp())) {
                log.info("Getting weather information for city {} from cache as temperature is same {}", city, entity.getTemperature());
            } else {
                entity = new WeatherEntity();
                entity.setCity(response.getName());
                entity.setCountry(response.getSys().getCountry());
                entity.setTemperature(response.getMain().getTemp());
                entity.setDateTime(response.getDt());
                log.info("Persisting city {} weather information...", city);
                entity = save(entity);
                log.info("City '{}' weather information persist successfully. Updating cache: {}",city, entity);
            }
            entity.setCallTimeMS(callTimeMs);
            // update cache
            addWeatherEntityToCache(city, entity);
            return entity;
        } catch (Exception e) {
            log.error("Unable to persist weather information for city {}; Reason: {}", city, e.getMessage(), e);
            throw e;
        }
    }


    /**
     * Check if need to make new weather api call
     * @param city
     * @return
     */
    private boolean makeCallToWeatherApi(String city){
        long expiredBefore = System.currentTimeMillis() - weatherConfig.getRequestWaitMs();
        WeatherEntity entity = getCachedWeatherEntity(city);
        return (entity == null) || (entity.getCallTimeMS() < expiredBefore);
    }

    /**
     * Get cached weatherEntity for a given city
     * @param city
     * @return
     */
    private WeatherEntity getCachedWeatherEntity(String city){
        return cityWeatherCacheMap.get(city.toLowerCase());
    }

    /**
     * Add weatherEntity to cache
     * @param city
     * @param entity
     */
    private void addWeatherEntityToCache(String city, WeatherEntity entity){
        cityWeatherCacheMap.put(city.toLowerCase(), entity);
    }
}
