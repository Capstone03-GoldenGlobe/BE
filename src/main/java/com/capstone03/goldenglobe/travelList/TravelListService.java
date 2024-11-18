package com.capstone03.goldenglobe.travelList;

import com.capstone03.goldenglobe.TravelAuthCheck;
import com.capstone03.goldenglobe.chatBot.ChatBot;
import com.capstone03.goldenglobe.chatBot.ChatBotRepository;
import com.capstone03.goldenglobe.checkList.CheckList;
import com.capstone03.goldenglobe.checkList.CheckListRepository;
import com.capstone03.goldenglobe.user.CustomUser;
import com.capstone03.goldenglobe.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

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
    // 날짜 검증 로직 추가
    if (travelList.getStartDate() == null || travelList.getEndDate() == null) {
      throw new IllegalArgumentException("시작일과 종료일은 필수 입력 사항입니다.");
    }

    if (travelList.getStartDate().isAfter(travelList.getEndDate())) {
      throw new IllegalArgumentException("시작일은 종료일보다 이전이어야 합니다.");
    }

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
    TravelList travelList = travelAuthCheck.isOkay(placeId, auth);

    // 날짜 검증 로직 추가
    if (travelListDTO.getStartDate() != null && travelListDTO.getEndDate() != null) {
      if (travelListDTO.getStartDate().isAfter(travelListDTO.getEndDate())) {
        throw new IllegalArgumentException("시작일은 종료일보다 이전이어야 합니다.");
      }
    }

    travelList.setCountry(travelListDTO.getCountry());
    travelList.setCity(travelListDTO.getCity());
    travelList.setStartDate(travelListDTO.getStartDate());
    travelList.setEndDate(travelListDTO.getEndDate());
    return travelListRepository.save(travelList);
  }
}
