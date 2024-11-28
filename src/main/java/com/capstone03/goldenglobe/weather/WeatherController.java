package com.capstone03.goldenglobe.weather;

import com.capstone03.goldenglobe.ApiResponseSetting;
import com.capstone03.goldenglobe.checkList.CheckListResponseDTO;
import com.capstone03.goldenglobe.travelList.TravelList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;
    @GetMapping("/temp/{travel_id}")
    public ResponseEntity<ApiResponseSetting<Double>> getAverageTemperature(@PathVariable("travel_id") Long travelId) {
        ApiResponseSetting<Double> response = weatherService.getTemperature(travelId);
        return ResponseEntity.ok(response);
    }
}
