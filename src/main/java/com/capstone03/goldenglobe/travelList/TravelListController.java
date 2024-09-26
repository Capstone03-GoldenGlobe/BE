package com.capstone03.goldenglobe.travelList;

import com.capstone03.goldenglobe.user.User;
import com.capstone03.goldenglobe.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/place")
@RequiredArgsConstructor
public class TravelListController {

  private final TravelListService travelListService;
  private final UserRepository userRepository;

  @GetMapping("/list")
  public ResponseEntity<List<TravelList>> getTravelList(
      @RequestParam(required = false) String country,
      @RequestParam(required = false) String city) {
    List<TravelList> travelLists = travelListService.getTravelList(country, city);
    return new ResponseEntity<>(travelLists, HttpStatus.OK);
  }

  @GetMapping("/intro/{place_id}")
  public ResponseEntity<TravelList> getTravelListById(@PathVariable("place_id") Long placeId) {
    TravelList travelList = travelListService.getTravelListById(placeId);
    if (travelList != null) {
      return new ResponseEntity<>(travelList, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/create")
  public ResponseEntity<TravelList> createTravelList(@RequestBody TravelListDto travelListDto) {
    User user = userRepository.findById(travelListDto.getUserId())
        .orElseThrow(() -> new RuntimeException("User not found with id: " + travelListDto.getUserId()));

    TravelList travelList = new TravelList();
    travelList.setUser(user);
    travelList.setCountry(travelListDto.getCountry());
    travelList.setCity(travelListDto.getCity());
    travelList.setStartDate(travelListDto.getStartDate());
    travelList.setEndDate(travelListDto.getEndDate());

    TravelList createdTravelList = travelListService.createTravelList(travelList);
    return new ResponseEntity<>(createdTravelList, HttpStatus.CREATED);
  }

  @PostMapping("/create/{dest_id}")
  public ResponseEntity<TravelListDto> createTravelListWithDestId(
      @PathVariable Long dest_id,
      @RequestBody TravelListDto travelListDto) {

    // userId로 User 조회
    User user = userRepository.findById(travelListDto.getUserId())
        .orElseThrow(() -> new RuntimeException("User not found with id: " + travelListDto.getUserId()));

    // TravelList 객체 생성
    TravelList travelList = new TravelList();
    travelList.setUser(user);
    travelList.setDestId(dest_id);
    travelList.setCountry(travelListDto.getCountry());
    travelList.setCity(travelListDto.getCity());
    travelList.setStartDate(travelListDto.getStartDate());
    travelList.setEndDate(travelListDto.getEndDate());

    TravelList createdTravelList = travelListService.createTravelList(travelList);
    TravelListDto dto = TravelListDto.fromEntity(createdTravelList);
    return new ResponseEntity<>(dto, HttpStatus.CREATED);
  }
}
