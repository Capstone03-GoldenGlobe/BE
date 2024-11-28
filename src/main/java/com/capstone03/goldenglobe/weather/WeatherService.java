package com.capstone03.goldenglobe.weather;

import com.capstone03.goldenglobe.ApiResponseSetting;
import com.capstone03.goldenglobe.travelList.TravelList;
import com.capstone03.goldenglobe.travelList.TravelListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final TravelListRepository travelListRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${weatherbit.api.key}")
    private String apiKey;

    public ApiResponseSetting<Double> getTemperature(Long destId) {
        // DB에서 여행 정보 가져오기
        TravelList travel = travelListRepository.findById(destId)
                .orElseThrow(() -> new IllegalArgumentException("여행지를 찾을 수 없습니다."));

        String city = travel.getCity(); // 한->영 번역 필요한 경우 있을 수도 (ex.이스탄불)
        LocalDate startDate = travel.getStartDate();
        LocalDate today = LocalDate.now();

        Double temperature;
        if (startDate.isBefore(today)) {
            // 과거 날짜라면 해당 날짜의 날씨 데이터를 반환
            temperature = getHistoricalTemperature(city, startDate, startDate.plusDays(1),apiKey);
            return new ApiResponseSetting<>(200, String.format("%s: 여행 시작일 기온 (이미 지난 여행)", city), temperature);
        } else if (startDate.isAfter(today.plusDays(7))) {
            // 일주일보다 넘게 남았다면 작년의 동일 날짜 데이터 반환
            LocalDate pastStartDate = startDate.minusYears(1);
            temperature = getHistoricalTemperature(city, pastStartDate, pastStartDate.plusDays(1),apiKey);
            return new ApiResponseSetting<>(200, String.format("%s: 1년 전 여행 시작일의 기온", city), temperature);
        } else {
            // 일주일 이내라면 예보 데이터 반환
            long days = ChronoUnit.DAYS.between(today, startDate) + 1;
            temperature =  getForecastTemperature(city, days,apiKey);
            return new ApiResponseSetting<>(200, String.format("%s: 여행 시작일 기온 (일기 예보)", city), temperature);
        }
    }

    public double getHistoricalTemperature(String city, LocalDate startDate, LocalDate endDate, String apiKey) {
        String url = String.format(
                "https://api.weatherbit.io/v2.0/history/daily?city=%s&start_date=%s&end_date=%s&key=%s",
                city, startDate, endDate, apiKey
        );
        try {
            WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);
            if (response == null || response.getData() == null || response.getData().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "날씨 데이터를 가져올 수 없습니다.");
            }
            return response.getData().get(0).getTemp();
        }  catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Weather API 호출 중 문제가 발생했습니다.", e);
        }
    }

    public double getForecastTemperature(String city, long days, String apiKey) {
        String url = String.format(
                "https://api.weatherbit.io/v2.0/forecast/daily?city=%s&days=%d&key=%s",
                city, days, apiKey
        );
        try {
            WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);
            if (response == null || response.getData() == null || response.getData().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "날씨 데이터를 가져올 수 없습니다.");
            }
            return response.getData().get(0).getTemp();
        }  catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Weather API 호출 중 문제가 발생했습니다.", e);
        }
    }
}
