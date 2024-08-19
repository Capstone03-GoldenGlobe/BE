package com.capstone03.goldenglobe.groupMemo;

import com.capstone03.goldenglobe.listGroup.ListGroup;
import com.capstone03.goldenglobe.listGroup.ListGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class GroupMemoService {
    private final GroupMemoRepository groupMemoRepository;
    private final ListGroupRepository listGroupRepository;

    public GroupMemo makeMemo(Long group_id, String memo){
        // 일치하는 그룹이 있는지 확인
        ListGroup listGroup = listGroupRepository.findById(group_id)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 group_id가 없음"));

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
}
