package com.capstone03.goldenglobe;

import com.capstone03.goldenglobe.sharedList.SharedList;
import com.capstone03.goldenglobe.sharedList.SharedListService;
import com.capstone03.goldenglobe.travelList.TravelList;
import com.capstone03.goldenglobe.travelList.TravelListService;
import com.capstone03.goldenglobe.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BasicController {

  private final UserService userService;
  private final TravelListService travelListService;
  private final SharedListService sharedListService;

  @GetMapping("/")
  public ResponseEntity<Map<String, Object>> getMainPageInfo() {

    String username = userService.getLoggedInUsername();
    List<TravelList> travelLists = travelListService.getTravelList(null, null);
    List<SharedList> sharedLists = sharedListService.getAllSharedLists();

    Map<String, Object> response = new HashMap<>();
    response.put("유저 이름", username);
    response.put("여행 리스트", travelLists);
    response.put("공유 체크리스트", sharedLists);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
