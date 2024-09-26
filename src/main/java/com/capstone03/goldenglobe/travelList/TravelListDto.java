package com.capstone03.goldenglobe.travelList;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TravelListDto {
  private Long userId;
  private String country;
  private String city;
  private LocalDate startDate;
  private LocalDate endDate;

  public static TravelListDto fromEntity(TravelList travelList) {
    TravelListDto dto = new TravelListDto();
    dto.setUserId(travelList.getUser().getUserId()); // User 객체에서 userId 추출
    dto.setCountry(travelList.getCountry());
    dto.setCity(travelList.getCity());
    dto.setStartDate(travelList.getStartDate());
    dto.setEndDate(travelList.getEndDate());
    return dto;
  }
}

