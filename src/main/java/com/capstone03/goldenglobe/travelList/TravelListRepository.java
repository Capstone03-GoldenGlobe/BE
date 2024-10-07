package com.capstone03.goldenglobe.travelList;

import com.capstone03.goldenglobe.checkList.CheckList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TravelListRepository extends JpaRepository<TravelList, Long> {
    Optional<TravelList> findByDestId(Long destId);
    List<TravelList> findByCountry(String country);
    List<TravelList> findByCity(String city);
    List<TravelList> findByCountryAndCity(String country, String city);
    List<TravelList> findByUser_UserId(Long userId);
}
