package com.capstone03.goldenglobe.travelList;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelListRepository extends JpaRepository<TravelList, Long> {
    TravelList findByDestId(Long destId);
}
