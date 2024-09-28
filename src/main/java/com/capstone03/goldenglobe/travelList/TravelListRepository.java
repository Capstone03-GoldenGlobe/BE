package com.capstone03.goldenglobe.travelList;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TravelListRepository extends JpaRepository<TravelList, Long> {
    TravelList findByDestId(Long destId);
    List<TravelList> findByCountry(String country);
    List<TravelList> findByCity(String city);
    List<TravelList> findByCountryAndCity(String country, String city);
    List<TravelList> findByUser_UserId(Long userId);
}
