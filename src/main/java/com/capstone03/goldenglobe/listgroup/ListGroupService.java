package com.capstone03.goldenglobe.listgroup;

import com.capstone03.goldenglobe.checklist.CheckList;
import com.capstone03.goldenglobe.checklist.CheckListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListGroupService {
    private final ListGroupRepository listGroupRepository;
    private final CheckListRepository checkListRepository;

    public ListGroup makeGroup(Long list_id, String group_name){
        // 일치하는 체크리스트가 있는지 확인
        CheckList checkList = checkListRepository.findById(list_id)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 list_id가 없음"));

        ListGroup listGroup = new ListGroup();
        listGroup.setList(checkList);
        listGroup.setGroupName(group_name);

        // 저장
        return listGroupRepository.save(listGroup);
    }
}
