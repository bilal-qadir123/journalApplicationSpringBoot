package com.project.journalProject.service;

import com.project.journalProject.api.response.WeatherResponse;
import com.project.journalProject.cache.AppCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherService {

    @Value("${weather.api.key}")
    private String API_KEY;

    private final RestTemplate restTemplate;

    private final AppCache appCache;

    public WeatherService(RestTemplate restTemplate, AppCache appCache) {
        this.restTemplate = restTemplate;
        this.appCache = appCache;
    }

    public WeatherResponse getWeather(String city) {
        String url = appCache.get(AppCache.keys.WEATHER_API.toString());

        if (url == null) {
            throw new RuntimeException("Weather API URL not configured in database");
        }

        url = url.replace("{city}", city)
                .replace("{API_KEY}", API_KEY);

        ResponseEntity<WeatherResponse> response = restTemplate.exchange(
                url, HttpMethod.GET, null, WeatherResponse.class);

        return response.getBody();
    }
}
