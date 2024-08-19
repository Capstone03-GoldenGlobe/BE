package com.capstone03.goldenglobe.checklist;

import com.capstone03.goldenglobe.UserRepository;
import com.capstone03.goldenglobe.groupmemo.GroupMemo;
import com.capstone03.goldenglobe.groupmemo.GroupMemoRepository;
import com.capstone03.goldenglobe.listgroup.ListGroup;
import com.capstone03.goldenglobe.listgroup.ListGroupRepository;
import com.capstone03.goldenglobe.listitem.ListItem;
import com.capstone03.goldenglobe.listitem.ListItemRepository;
import com.capstone03.goldenglobe.travellist.TravelList;
import com.capstone03.goldenglobe.User;
import com.capstone03.goldenglobe.travellist.TravelListRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CheckListService {
    private final CheckListRepository checkListRepository;
    private final TravelListRepository travelListRepository;
    private final UserRepository userRepository;
    private final GroupMemoRepository groupMemoRepository;
    private final ListGroupRepository listGroupRepository;
    private final ListItemRepository listItemRepository;

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

    public Map<String, Object> getCheckListDetails(Long dest_id) {
        // 1. CheckList 조회
        Optional<CheckList> optionalCheckList = checkListRepository.findById(dest_id);

        if (optionalCheckList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "체크리스트를 찾을 수 없습니다.");
        }

        CheckList checkList = optionalCheckList.get();

        // 2. ListGroup 조회
        List<ListGroup> listGroups = listGroupRepository.findByList_ListId(checkList.getListId());

        // 3. GroupMemo 조회 및 4. ListItem 조회
        List<Map<String, Object>> groupsData = listGroups.stream().map(group -> {
            // GroupMemo 조회
            Optional<GroupMemo> GroupMemo = groupMemoRepository.findByGroup_GroupId(group.getGroupId());

            // ListItem 조회
            List<ListItem> listItems = listItemRepository.findByGroup_GroupId(group.getGroupId());

            // Map으로 구성
            Map<String, Object> groupData = new HashMap<>();
            groupData.put("group_id", group.getGroupId().toString());
            groupData.put("group_name", group.getGroupName());

            // GroupMemo가 존재하는 경우
            GroupMemo.ifPresent(memo -> {
                groupData.put("memo_id", memo.getMemoId().toString());
                groupData.put("memo", memo.getMemo());
            });

            // ListItem을 포함하는 항목 구성
            List<Map<String, Object>> itemsData = listItems.stream().map(item -> {
                Map<String, Object> itemData = new HashMap<>();
                itemData.put("item_id", item.getItemId().toString());
                itemData.put("item_name", item.getItem());
                itemData.put("check", item.isChecked());

                // user가 null인 경우 처리
                if (item.getUser() != null) {
                    itemData.put("user_id", item.getUser().getUserId().toString());
                } else {
                    itemData.put("user_id", null); // 또는 "user_id", "null" 등을 사용할 수 있습니다.
                }
                return itemData;
            }).collect(Collectors.toList());

            groupData.put("items", itemsData);
            return groupData;
        }).collect(Collectors.toList());

        // 응답 데이터 구성
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "체크리스트 조회 성공");

        Map<String, Object> data = new HashMap<>();
        data.put("checklist", List.of(Map.of("list_id", checkList.getListId().toString())));
        data.put("groups", groupsData);

        response.put("data", data);
        return response;
    }
}