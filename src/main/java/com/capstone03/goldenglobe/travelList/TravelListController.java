package com.capstone03.goldenglobe.travelList;

import com.capstone03.goldenglobe.user.CustomUser;
import com.capstone03.goldenglobe.user.User;
import com.capstone03.goldenglobe.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
  public ResponseEntity<TravelList> createTravelList(@RequestBody TravelListDTO travelListDto, Authentication auth) {
    CustomUser customUser = (CustomUser) auth.getPrincipal();
    Optional<User> user = userRepository.findById(customUser.getId());

    TravelList travelList = new TravelList();
    travelList.setUser(user.get());
    travelList.setCountry(travelListDto.getCountry());
    travelList.setCity(travelListDto.getCity());
    travelList.setStartDate(travelListDto.getStartDate());
    travelList.setEndDate(travelListDto.getEndDate());

    TravelList createdTravelList = travelListService.createTravelList(travelList);
    TravelListDTO toDto = TravelListDTO.fromEntity(createdTravelList);
    return new ResponseEntity<>(createdTravelList, HttpStatus.CREATED);
  }

//  @PostMapping("/create/{dest_id}")
//  public ResponseEntity<TravelListDTO> createTravelListWithDestId(
//          @PathVariable Long dest_id,
//          @RequestBody TravelListDTO travelListDto) {
//
//    // userId로 User 조회
//    User user = userRepository.findById(travelListDto.getUserId())
//            .orElseThrow(() -> new RuntimeException("User not found with id: " + travelListDto.getUserId()));
//
//    // TravelList 객체 생성
//    TravelList travelList = new TravelList();
//    travelList.setUser(user);
//    travelList.setDestId(dest_id);
//    travelList.setCountry(travelListDto.getCountry());
//    travelList.setCity(travelListDto.getCity());
//    travelList.setStartDate(travelListDto.getStartDate());
//    travelList.setEndDate(travelListDto.getEndDate());
//
//    TravelList createdTravelList = travelListService.createTravelList(travelList);
//    TravelListDTO dto = TravelListDTO.fromEntity(createdTravelList);
//    return new ResponseEntity<>(dto, HttpStatus.CREATED);
//  }
}
