package com.project.journalProject.service;

import com.project.journalProject.api.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherService {

    @Value("${weather.api.key}")
    private String API_KEY;

    private final RestTemplate restTemplate;

    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public WeatherResponse getWeather(String city) {
        String url = "https://api.openweathermap.org/data/2.5/weather"
                + "?q=" + city
                + "&units=metric"
                + "&lang=en"
                + "&appid=" + API_KEY;

        return restTemplate.getForObject(url, WeatherResponse.class);
    }
}
