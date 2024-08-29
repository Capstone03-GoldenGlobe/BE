package com.capstone03.goldenglobe;

import com.capstone03.goldenglobe.checkList.CheckList;
import com.capstone03.goldenglobe.checkList.CheckListRepository;
import com.capstone03.goldenglobe.listGroup.ListGroup;
import com.capstone03.goldenglobe.listGroup.ListGroupRepository;
import com.capstone03.goldenglobe.listItem.ListItem;
import com.capstone03.goldenglobe.listItem.ListItemRepository;
import com.capstone03.goldenglobe.sharedList.SharedListRepository;
import com.capstone03.goldenglobe.user.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckListAuthCheck {

    private final CheckListRepository checkListRepository;
    private final SharedListRepository sharedListRepository;
    private final ListGroupRepository listGroupRepository;
    private final ListItemRepository listItemRepository;

    public boolean hasAccessToCheckList(Long checkListId, Authentication auth) {
        // CheckList 존재 여부 확인
        CheckList checkList = checkListRepository.findByListId(checkListId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "체크리스트를 찾을 수 없습니다."));

        // 유저 권한 확인
        CustomUser customUser = (CustomUser) auth.getPrincipal();
        Long authUserId = customUser.getId();

        // 체크리스트 소유자 확인
        if (checkList.getUser().getUserId().equals(authUserId)) {
            return true;
        }

        // 체크리스트 공유 여부 확인
        return sharedListRepository.existsByList_ListIdAndUser_UserId(checkList.getListId(), authUserId);
    }

    public CheckList findAndCheckAccessToList(Long checkListId, Authentication auth) {
        // CheckList 존재 여부 확인
        CheckList checkList = checkListRepository.findByListId(checkListId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "체크리스트를 찾을 수 없습니다."));

        // 유저 권한 확인 절차
        if (!hasAccessToCheckList(checkListId, auth)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "체크리스트에 접근할 수 없습니다.");
        }

        return checkList;
    }

    public ListGroup findAndCheckAccessToGroup(Long groupId, Authentication auth) {
        // 그룹 조회 및 존재하지 않을 경우 예외 처리
        ListGroup listGroup = listGroupRepository.findByGroupId(groupId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "일치하는 group_id가 없음"));

        // 유저 권한 확인 절차
        if (!hasAccessToCheckList(listGroup.getList().getListId(), auth)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "체크리스트에 접근할 수 없습니다.");
        }

        return listGroup;
    }


    public ListItem findAndCheckAccessToItem(Long itemId, Authentication auth) {
        // 아이템 조회 및 존재하지 않을 경우 예외 처리
        ListItem listItem = listItemRepository.findByItemId(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "일치하는 item_id가 없음"));

        // 체크리스트 권한 확인
        if (!hasAccessToCheckList(listItem.getList().getListId(), auth)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "체크리스트에 접근할 수 없습니다.");
        }

        return listItem;
    }
}