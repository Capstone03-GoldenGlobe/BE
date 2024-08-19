package com.capstone03.goldenglobe.listitem;

import com.capstone03.goldenglobe.checklist.CheckList;
import com.capstone03.goldenglobe.checklist.CheckListRepository;
import com.capstone03.goldenglobe.listgroup.ListGroup;
import com.capstone03.goldenglobe.listgroup.ListGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListItemService {
    private final ListItemRepository listItemRepository;
    private final CheckListRepository checkListRepository;
    private final ListGroupRepository listGroupRepository;
    public ListItem makeItem(Long list_id, Long group_id, String item_name) {

        ListItem listItem = new ListItem();

        // 일치하는 체크리스트가 있는지 확인
        CheckList checkList = checkListRepository.findById(list_id)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 list_id가 없음"));

        // 일치하는 그룹이 있는지 확인
        ListGroup listGroup = listGroupRepository.findById(group_id)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 group_id가 없음"));

        listItem.setList(checkList);
        listItem.setGroup(listGroup);

        listItem.setItem(item_name);

        // 저장
        return listItemRepository.save(listItem);
    }
}
