package com.capstone03.goldenglobe.checkList;

import com.capstone03.goldenglobe.CheckListAuthCheck;
import com.capstone03.goldenglobe.user.CustomUser;
import com.capstone03.goldenglobe.groupMemo.GroupMemo;
import com.capstone03.goldenglobe.groupMemo.GroupMemoRepository;
import com.capstone03.goldenglobe.listGroup.ListGroup;
import com.capstone03.goldenglobe.listGroup.ListGroupRepository;
import com.capstone03.goldenglobe.listItem.ListItem;
import com.capstone03.goldenglobe.listItem.ListItemRepository;
import com.capstone03.goldenglobe.travelList.TravelList;
import com.capstone03.goldenglobe.user.User;
import com.capstone03.goldenglobe.travelList.TravelListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CheckListService {
    private final CheckListRepository checkListRepository;
    private final TravelListRepository travelListRepository;
    private final GroupMemoRepository groupMemoRepository;
    private final ListGroupRepository listGroupRepository;
    private final ListItemRepository listItemRepository;

    private final CheckListAuthCheck authCheck;

    public String makeCheckList(Long destId, Authentication auth){
        TravelList travelList = (TravelList) travelListRepository.findByDestId(destId);
        CheckList checkList = new CheckList();
        checkList.setDest(travelList);

        CustomUser customUser = (CustomUser) auth.getPrincipal();
        var user = new User();
        user.setUserId(customUser.getId());
        checkList.setUser(user);
        checkList = checkListRepository.save(checkList);

        return checkList.getListId().toString();
    }

    public CheckListResponseDTO getCheckListDetails(Long destId, Authentication auth) {
        // 1. dest_id로 CheckList 조회
        Optional<CheckList> optionalCheckList = checkListRepository.findByDest_DestId(destId);
        if (optionalCheckList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "체크리스트를 찾을 수 없습니다.");
        }

        CheckList checkList = optionalCheckList.get();

        // 유저 권한 확인 절차 >> 수정 필요!!!!
        if (!authCheck.hasAccessToCheckList(checkList.getListId(), auth)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "체크리스트에 접근할 수 없습니다.");
        }

        // 2. ListGroup 조회
        List<ListGroup> listGroups = listGroupRepository.findByList_ListId(checkList.getListId());

        // 3. GroupMemo 조회 및 4. ListItem 조회
        List<GroupResponseDTO> groupsData = listGroups.stream().map(group -> {
            // GroupMemo 조회
            Optional<GroupMemo> groupMemo = groupMemoRepository.findByGroup_GroupId(group.getGroupId());

            // ListItem 조회
            List<ListItem> listItems = listItemRepository.findByGroup_GroupId(group.getGroupId());

            // GroupResponseDto 생성
            GroupResponseDTO groupResponse = new GroupResponseDTO();
            groupResponse.setGroupId(group.getGroupId().toString());
            groupResponse.setGroupName(group.getGroupName());

            // GroupMemo가 존재하는 경우
            groupMemo.ifPresent(memo -> {
                groupResponse.setMemoId(memo.getMemoId().toString());
                groupResponse.setMemo(memo.getMemo());
            });

            // ListItem을 포함하는 항목 구성
            List<ItemResponseDTO> itemsData = listItems.stream().map(item -> {
                ItemResponseDTO itemResponse = new ItemResponseDTO();
                itemResponse.setItemId(item.getItemId().toString());
                itemResponse.setItemName(item.getItem());
                itemResponse.setCheck(item.isChecked());

                // user가 null인 경우 처리
                itemResponse.setUserId(item.getUser() != null ? item.getUser().getUserId().toString() : null);

                return itemResponse;
            }).collect(Collectors.toList());

            groupResponse.setItems(itemsData);
            return groupResponse;
        }).collect(Collectors.toList());

        // CheckListResponseDto 생성
        return new CheckListResponseDTO(checkList.getListId().toString(), groupsData);
    }
}