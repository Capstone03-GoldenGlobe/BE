package com.capstone03.goldenglobe.listItem;

import com.capstone03.goldenglobe.CheckListAuthCheck;
import com.capstone03.goldenglobe.checkList.CheckList;
import com.capstone03.goldenglobe.checkList.CheckListRepository;
import com.capstone03.goldenglobe.listGroup.ListGroup;
import com.capstone03.goldenglobe.listGroup.ListGroupRepository;
import com.capstone03.goldenglobe.user.CustomUser;
import com.capstone03.goldenglobe.user.User;
import com.capstone03.goldenglobe.user.UserRepository;
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
    private final UserRepository userRepository;
    private final CheckListAuthCheck authCheck;
    public ListItem makeItem(Long listId, Long groupId, String itemName, Authentication auth) {

        ListItem listItem = new ListItem();

        // 일치하는 체크리스트가 있는지 확인
        CheckList checkList = checkListRepository.findById(listId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "일치하는 list_id가 없음"));

        // 일치하는 그룹이 있는지 확인
        ListGroup listGroup = authCheck.findAndCheckAccessToGroup(groupId,auth);

        listItem.setList(checkList);
        listItem.setGroup(listGroup);

        listItem.setItem(itemName);

        // 저장
        return listItemRepository.save(listItem);
    }

    public ListItem editItemName(Long itemId, String itemName, Authentication auth){
        ListItem listItem = authCheck.findAndCheckAccessToItem(itemId,auth);
        listItem.setItem(itemName);
        return listItemRepository.save(listItem);
    }

    public ListItem editItemChecked(Long itemId, Authentication auth){
        ListItem listItem = authCheck.findAndCheckAccessToItem(itemId,auth);
        listItem.setChecked(!listItem.isChecked());
         if(listItem.isChecked()){ // true
             CustomUser customUser = (CustomUser) auth.getPrincipal();
             String userPhone = customUser.getCellphone();
             User user = userRepository.findByCellphone(userPhone)
                     .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));

             // 아이템에 유저 설정
             listItem.setUser(user);
         } else { // false
            listItem.setUser(null);
         }
        return listItemRepository.save(listItem);
    }

    public ListItem editItemGroup(Long itemId, Long newGroupId, Authentication auth){
        ListItem listItem = authCheck.findAndCheckAccessToItem(itemId,auth);
        Optional<ListGroup> group = listGroupRepository.findByGroupId(newGroupId);
        if(group.isPresent()){
            listItem.setGroup(group.get());
            return listItemRepository.save(listItem);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "일치하는 group_id가 없음");
        }
    }

    public void deleteItem(Long itemId, Authentication auth) {
        authCheck.findAndCheckAccessToItem(itemId,auth);
        // 아이템 삭제
        listItemRepository.deleteById(itemId);
    }
}
