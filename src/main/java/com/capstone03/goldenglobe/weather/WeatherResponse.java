package com.capstone03.goldenglobe.weather;

import lombok.Data;

import java.util.List;

@Data
public class WeatherResponse {
    private String city_name;
    private String country_code;
    private List<WeatherData> data;

    @Data
    public static class WeatherData {
        private double temp; // JSON의 temp 값을 매핑
        private String datetime;
    }
}