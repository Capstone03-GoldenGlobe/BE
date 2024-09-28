package com.capstone03.goldenglobe.travelList;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelListDTO {
  private String country;
  private String city;
  private LocalDate startDate;
  private LocalDate endDate;

  public static TravelListDTO fromEntity(TravelList travelList) {
    return new TravelListDTO(
            travelList.getCountry(),
            travelList.getCity(),
            travelList.getStartDate(),
            travelList.getEndDate()
    );
  }
}

