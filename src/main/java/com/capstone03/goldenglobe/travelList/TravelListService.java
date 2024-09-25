package com.capstone03.goldenglobe.travelList;

import com.capstone03.goldenglobe.checkList.CheckList;
import com.capstone03.goldenglobe.checkList.CheckListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TravelListService {

  private final TravelListRepository travelListRepository;
  private final CheckListRepository checkListRepository;

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
    TravelList savedTravelList = travelListRepository.save(travelList);

    // 체크리스트 자동 생성되도록
    CheckList checkList = new CheckList();
    checkList.setUser(savedTravelList.getUser());
    checkList.setDest(savedTravelList);
    checkListRepository.save(checkList);

    return savedTravelList;
  }
}
