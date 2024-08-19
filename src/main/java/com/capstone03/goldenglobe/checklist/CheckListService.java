package com.capstone03.goldenglobe.checklist;

import com.capstone03.goldenglobe.travellist.TravelList;
import com.capstone03.goldenglobe.User;
import com.capstone03.goldenglobe.travellist.TravelListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckListService {
    private final CheckListRepository checkListRepository;
    private final TravelListRepository travelListRepository;

    public String makeCheckList(String destId){
        TravelList travelList = (TravelList) travelListRepository.findByDestId(destId);
        CheckList checkList = new CheckList();
        checkList.setDest(travelList);

        //User currentUser = getCurrentUser();
        //checkList.setUser(currentUser);

        checkList = checkListRepository.save(checkList);

        return checkList.getListId().toString();
    }
}