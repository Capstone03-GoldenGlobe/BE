package com.capstone03.goldenglobe.checklist;

import com.capstone03.goldenglobe.UserRepository;
import com.capstone03.goldenglobe.travellist.TravelList;
import com.capstone03.goldenglobe.User;
import com.capstone03.goldenglobe.travellist.TravelListRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckListService {
    private final CheckListRepository checkListRepository;
    private final TravelListRepository travelListRepository;
    private final UserRepository userRepository;

    public String makeCheckList(Long destId){
        TravelList travelList = (TravelList) travelListRepository.findByDestId(destId);
        CheckList checkList = new CheckList();
        checkList.setDest(travelList);

        //User currentUser = getCurrentUser();
        //checkList.setUser(currentUser);
        // user_id가 1인 User 조회
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("User with ID 1 not found"));
        checkList.setUser(user);

        checkList = checkListRepository.save(checkList);

        return checkList.getListId().toString();
    }
}