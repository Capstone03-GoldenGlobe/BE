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

    public ListGroup makeGroup(Long list_id, String group_name, Authentication auth){
        // 일치하는 체크리스트가 있는지 확인
        CheckList checkList = checkListRepository.findById(list_id)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 list_id가 없음"));

        // 유저 권한 확인
        // 유저 권한 확인 절차
        if (!authCheck.hasAccessToCheckList(list_id, auth)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "체크리스트에 접근할 수 없습니다.");
        }

        ListGroup listGroup = new ListGroup();
        listGroup.setList(checkList);
        listGroup.setGroupName(group_name);

        // 저장
        return listGroupRepository.save(listGroup);
    }

    public ListGroup editGroupName(Long group_id, String group_name){
        Optional<ListGroup> group = listGroupRepository.findByGroupId(group_id);
        if (group.isPresent()) {
            ListGroup listGroup = group.get();
            listGroup.setGroupName(group_name);
            return listGroupRepository.save(listGroup);
        } else {
            throw new IllegalArgumentException("일치하는 group_id가 없음");
        }
    }
}
