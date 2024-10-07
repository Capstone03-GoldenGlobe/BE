package com.capstone03.goldenglobe.travelList;

import com.azure.core.annotation.Put;
import com.capstone03.goldenglobe.ApiResponseSetting;
import com.capstone03.goldenglobe.checkList.CheckListResponseDTO;
import com.capstone03.goldenglobe.user.CustomUser;
import com.capstone03.goldenglobe.user.User;
import com.capstone03.goldenglobe.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.Path;

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

  @PutMapping("/{place_id}")
  public ResponseEntity<ApiResponseSetting<TravelListResponseDTO>> updateTravelList(@PathVariable("place_id") Long placeId, @RequestBody TravelListDTO travelListDTO, Authentication auth){
    TravelList travelList = travelListService.updateTravelList(placeId,travelListDTO,auth);
    TravelListResponseDTO toDto = TravelListResponseDTO.fromEntity(travelList);
    ApiResponseSetting<TravelListResponseDTO> response = new ApiResponseSetting<>(200, "여행지 수정 성공", toDto);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/create")
  public ResponseEntity<ApiResponseSetting<TravelListResponseDTO>> createTravelList(@RequestBody TravelListDTO travelListDto, Authentication auth) {
    CustomUser customUser = (CustomUser) auth.getPrincipal();
    Optional<User> user = userRepository.findById(customUser.getId());

    TravelList travelList = new TravelList();
    travelList.setUser(user.get());
    travelList.setCountry(travelListDto.getCountry());
    travelList.setCity(travelListDto.getCity());
    travelList.setStartDate(travelListDto.getStartDate());
    travelList.setEndDate(travelListDto.getEndDate());

    TravelList createdTravelList = travelListService.createTravelList(travelList);
    TravelListResponseDTO toDto = TravelListResponseDTO.fromEntity(createdTravelList);
    ApiResponseSetting<TravelListResponseDTO> response = new ApiResponseSetting<>(200, "여행지 생성 성공", toDto);
    return ResponseEntity.ok(response);
  }
}
