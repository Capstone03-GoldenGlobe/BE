package com.capstone03.goldenglobe.travelList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TravelListService {

  @Autowired
  private TravelListRepository travelListRepository;

  public List<TravelList> getTravelList(String country, String city) {
    if (country != null && city != null) {
      return travelListRepository.findByCountryAndCity(country, city);
    } else if (country != null) {
      return travelListRepository.findByCountry(country);
    } else if (city != null) {
      return travelListRepository.findByCity(city);
    }
    return travelListRepository.findAll();
  }

  public TravelList getTravelListById(Long destId) {
    return travelListRepository.findById(destId).orElse(null);
  }

  public TravelList createTravelList(TravelList travelList) {
    return travelListRepository.save(travelList);
  }
}
