package com.capstone03.goldenglobe;

import com.capstone03.goldenglobe.checkList.CheckList;
import com.capstone03.goldenglobe.checkList.CheckListRepository;
import com.capstone03.goldenglobe.sharedList.SharedList;
import com.capstone03.goldenglobe.sharedList.SharedListRepository;
import com.capstone03.goldenglobe.sharedList.SharedListService;
import com.capstone03.goldenglobe.travelList.TravelList;
import com.capstone03.goldenglobe.travelList.TravelListRepository;
import com.capstone03.goldenglobe.travelList.TravelListService;
import com.capstone03.goldenglobe.user.CustomUser;
import com.capstone03.goldenglobe.user.User;
import com.capstone03.goldenglobe.user.UserRepository;
import com.capstone03.goldenglobe.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class BasicController {

  private final TravelListRepository travelListRepository;
  private final SharedListRepository sharedListRepository;
  private final CheckListRepository checkListRepository;
  private final UserRepository userRepository;

  @GetMapping("/")
  public ResponseEntity<Map<String, Object>> getMainPageInfo(Authentication auth) {

    CustomUser customUser = (CustomUser) auth.getPrincipal();
    Optional<User> user = userRepository.findById(customUser.getId());

    String username = user.get().getNickname();
    List<TravelList> travelLists = travelListRepository.findByUser_UserId(user.get().getUserId());

    // SharedList를 토대로 List 끌고오기
    List<TravelList> sharedTravelLists = new ArrayList<>();

    List<SharedList> sharedLists = sharedListRepository.findByUser_UserId(user.get().getUserId());
    for (SharedList sharedList : sharedLists) {
      Long checklistId = sharedList.getList().getListId(); // SharedList에서 CheckList 가져오기
      TravelList dest = checkListRepository.findById(checklistId).get().getDest(); // CheckList에서 TravelList 가져오기
      if (dest != null) {
        sharedTravelLists.add(dest);
      }
    }

    Map<String, Object> response = new HashMap<>();
    response.put("nickname", username);
    response.put("travelLists", travelLists);
    response.put("shared", sharedTravelLists);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
