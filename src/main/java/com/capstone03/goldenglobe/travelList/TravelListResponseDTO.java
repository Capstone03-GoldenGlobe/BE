package com.capstone03.goldenglobe.travelList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelListResponseDTO {
    private Long destId;
    private String country;
    private String city;
    private LocalDate startDate;
    private LocalDate endDate;

    public static TravelListResponseDTO fromEntity(TravelList travelList) {
        return new TravelListResponseDTO(
                travelList.getDestId(),
                travelList.getCountry(),
                travelList.getCity(),
                travelList.getStartDate(),
                travelList.getEndDate()
        );
    }
}
