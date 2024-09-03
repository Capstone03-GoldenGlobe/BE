package com.capstone03.goldenglobe.listGroup;

import com.capstone03.goldenglobe.CheckListAuthCheck;
import com.capstone03.goldenglobe.checkList.CheckList;
import com.capstone03.goldenglobe.checkList.CheckListRepository;
import com.capstone03.goldenglobe.user.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ListGroupService {
    private final ListGroupRepository listGroupRepository;
    private final CheckListRepository checkListRepository;

    private final CheckListAuthCheck authCheck;

    public ListGroup makeGroup(Long listId, String groupName, Authentication auth){
        // 일치하는 체크리스트가 있는지 확인
        CheckList checkList = authCheck.findAndCheckAccessToList(listId,auth);

        ListGroup listGroup = new ListGroup();
        listGroup.setList(checkList);
        listGroup.setGroupName(groupName);

        // 저장
        return listGroupRepository.save(listGroup);
    }

    public ListGroup editGroupName(Long groupId, String groupName, Authentication auth){
        // 그룹 조회 및 존재하지 않을 경우 예외 처리
        ListGroup listGroup = authCheck.findAndCheckAccessToGroup(groupId,auth);

        // 그룹 이름 변경
        listGroup.setGroupName(groupName);

        // 변경된 그룹 저장 후 반환
        return listGroupRepository.save(listGroup);
    }

    public void deleteGroup(Long groupId, Authentication auth) {
        authCheck.findAndCheckAccessToGroup(groupId, auth);
        listGroupRepository.deleteById(groupId);
    }
}
