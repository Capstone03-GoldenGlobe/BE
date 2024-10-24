package com.capstone03.goldenglobe.travelList;

import com.capstone03.goldenglobe.TravelAuthCheck;
import com.capstone03.goldenglobe.chatBot.ChatBot;
import com.capstone03.goldenglobe.chatBot.ChatBotRepository;
import com.capstone03.goldenglobe.checkList.CheckList;
import com.capstone03.goldenglobe.checkList.CheckListRepository;
import com.capstone03.goldenglobe.user.CustomUser;
import com.capstone03.goldenglobe.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TravelListService {

  private final TravelListRepository travelListRepository;
  private final CheckListRepository checkListRepository;
  private final ChatBotRepository chatBotRepository;
  private final TravelAuthCheck travelAuthCheck;

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

    // 챗봇 자동 생성
    ChatBot chatBot = new ChatBot();
    chatBot.setDest(savedTravelList);
    chatBotRepository.save(chatBot);

    return savedTravelList;
  }

  public TravelList updateTravelList(Long placeId, TravelListDTO travelListDTO, Authentication auth){
    TravelList travelList = travelAuthCheck.isOkay(placeId,auth);
    travelList.setCountry(travelListDTO.getCountry());
    travelList.setCity(travelListDTO.getCity());
    travelList.setStartDate(travelListDTO.getStartDate());
    travelList.setEndDate(travelListDTO.getEndDate());
    return travelListRepository.save(travelList);
  }

  public void deleteTravelList(Long placeId, Authentication auth){
    if(!travelListRepository.existsById(placeId)){ // 여행지 존재 여부확인
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "일치하는 여행지가 없습니다.");
    }
    travelListRepository.deleteById(placeId);
  }
}
