package com.capstone03.goldenglobe.groupMemo;

import com.capstone03.goldenglobe.CheckListAuthCheck;
import com.capstone03.goldenglobe.listGroup.ListGroup;
import com.capstone03.goldenglobe.listGroup.ListGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupMemoService {
    private final GroupMemoRepository groupMemoRepository;
    private final ListGroupRepository listGroupRepository;
    private final CheckListAuthCheck authCheck;

    public GroupMemo makeMemo(Long group_id, String memo, Authentication auth){
        ListGroup listGroup = authCheck.findAndCheckAccessToGroup(group_id,auth);
        try {
            GroupMemo groupMemo = new GroupMemo();
            groupMemo.setGroup(listGroup);
            groupMemo.setMemo(memo);
            // 저장
            return groupMemoRepository.save(groupMemo);
        } catch (DataIntegrityViolationException e) { // 무결성 위반 -> 중복된 그룹에 메모 추가하려고 할 때
            // DuplicateKeyException e 유니크 제약 조건 위반
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "데이터 무결성 위반", e);
        }
    }

    public GroupMemo editMemo(Long group_id, String memo, Authentication auth){
        authCheck.findAndCheckAccessToGroup(group_id,auth);
        Optional<GroupMemo> item = groupMemoRepository.findByGroup_GroupId(group_id);
        if (item.isPresent()) {
            GroupMemo groupMemo = item.get();
            groupMemo.setMemo(memo);
            return groupMemoRepository.save(groupMemo);
        } else {
            throw new IllegalArgumentException("해당 group_id로 메모를 찾을 수 없습니다. 메모를 먼저 생성해주세요");
        }
    }
}
