package com.capstone03.goldenglobe.weather;

import com.capstone03.goldenglobe.ApiResponseSetting;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;
    @GetMapping("/temp/{dest_id}")
    public ResponseEntity<ApiResponseSetting<Double>> getAverageTemperature(@PathVariable("dest_id") Long destId) {
        ApiResponseSetting<Double> response = weatherService.getTemperature(destId);
        return ResponseEntity.ok(response);
    }
}
