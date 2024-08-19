package com.capstone03.goldenglobe.travellist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TravelListRepository extends JpaRepository<TravelList, Long> {
    List<TravelList> findByDestId(Long destId);
}
