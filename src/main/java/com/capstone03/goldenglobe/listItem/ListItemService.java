package com.capstone03.goldenglobe.listItem;

import com.capstone03.goldenglobe.CheckListAuthCheck;
import com.capstone03.goldenglobe.checkList.CheckList;
import com.capstone03.goldenglobe.checkList.CheckListRepository;
import com.capstone03.goldenglobe.listGroup.ListGroup;
import com.capstone03.goldenglobe.listGroup.ListGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ListItemService {
    private final ListItemRepository listItemRepository;
    private final CheckListRepository checkListRepository;
    private final ListGroupRepository listGroupRepository;
    private final CheckListAuthCheck authCheck;
    public ListItem makeItem(Long list_id, Long group_id, String item_name, Authentication auth) {

        ListItem listItem = new ListItem();

        // 일치하는 체크리스트가 있는지 확인
        CheckList checkList = checkListRepository.findById(list_id)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 list_id가 없음"));

        // 일치하는 그룹이 있는지 확인
        ListGroup listGroup = authCheck.findAndCheckAccessToGroup(group_id,auth);

        listItem.setList(checkList);
        listItem.setGroup(listGroup);

        listItem.setItem(item_name);

        // 저장
        return listItemRepository.save(listItem);
    }

    public ListItem editItemName(Long item_id, String item_name, Authentication auth){
        ListItem listItem = authCheck.findAndCheckAccessToItem(item_id,auth);
        listItem.setItem(item_name);
        return listItemRepository.save(listItem);
    }

    public ListItem editItemChecked(Long item_id){
        Optional<ListItem> item = listItemRepository.findByItemId(item_id);
        if (item.isPresent()) {
            ListItem listItem = item.get();
            listItem.setChecked(!listItem.isChecked());
//             if(checked==True){
//                // auth로 사용자 받아서
//                listItem.setUser(유저아이디);
//             } else {
//                listItem.setUser(Null);
//             }
            ListItem updatedItem = listItemRepository.save(listItem);
            return updatedItem;
        } else {
            throw new IllegalArgumentException("일치하는 item_id가 없음");
        }
    }

    public ListItem editItemGroup(Long item_id, Long new_group_id, Authentication auth){
        ListItem listItem = authCheck.findAndCheckAccessToItem(item_id,auth);
        Optional<ListGroup> group = listGroupRepository.findByGroupId(new_group_id);
        if(group.isPresent()){
            listItem.setGroup(group.get());
            return listItemRepository.save(listItem);
        } else {
            throw new IllegalArgumentException("일치하는 group_id가 없음");
        }
    }
}
