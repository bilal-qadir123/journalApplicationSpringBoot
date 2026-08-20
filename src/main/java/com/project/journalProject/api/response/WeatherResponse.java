package com.project.journalProject.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WeatherResponse {

    private List<Weather> weather;
    private Main main;

    @Getter
    @Setter
    public static class Weather {

        private String description;
    }

    @Getter
    @Setter
    public static class Main {

        private double temp;

        @JsonProperty("feels_like")
        private double feelsLike;
    }
}
