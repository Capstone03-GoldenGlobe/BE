package com.capstone03.goldenglobe.travelList;

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
  public ResponseEntity<TravelList> createTravelList(@RequestBody TravelList travelList) {
    if (travelList.getUser() == null || travelList.getUser().getUserId() == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    TravelList createdTravelList = travelListService.createTravelList(travelList);
    return new ResponseEntity<>(createdTravelList, HttpStatus.CREATED);
  }

  @PostMapping("/create/{dest_id}")
  public ResponseEntity<TravelList> createTravelListWithDestId(
      @PathVariable Long dest_id,
      @RequestBody TravelList travelList) {
    if (travelList.getUser() == null || travelList.getUser().getUserId() == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    travelList.setDestId(dest_id);
    TravelList createdTravelList = travelListService.createTravelList(travelList);
    return new ResponseEntity<>(createdTravelList, HttpStatus.CREATED);
  }
}
