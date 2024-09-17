package com.capstone03.goldenglobe.groupMemo;

import com.capstone03.goldenglobe.CheckListAuthCheck;
import com.capstone03.goldenglobe.listGroup.ListGroup;
import com.capstone03.goldenglobe.listGroup.ListGroupRepository;
import jakarta.transaction.Transactional;
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

    public GroupMemo makeMemo(Long groupId, String memo, Authentication auth){
        ListGroup listGroup = authCheck.findAndCheckAccessToGroup(groupId,auth);
        try {
            GroupMemo groupMemo = new GroupMemo();
            groupMemo.setGroup(listGroup);
            groupMemo.setMemo(memo);
            // 저장
            return groupMemoRepository.save(groupMemo);
        } catch (DataIntegrityViolationException e) { // 무결성 위반 -> 중복된 그룹에 메모 추가하려고 할 때
            // DuplicateKeyException e 유니크 제약 조건 위반
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "데이터 무결성 위반: " +e.getMessage());
        }
    }

    public GroupMemo editMemo(Long groupId, String memo, Authentication auth){
        authCheck.findAndCheckAccessToGroup(groupId,auth);
        Optional<GroupMemo> item = groupMemoRepository.findByGroup_GroupId(groupId);
        if (item.isPresent()) {
            GroupMemo groupMemo = item.get();
            groupMemo.setMemo(memo);
            return groupMemoRepository.save(groupMemo);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 group_id로 메모를 찾을 수 없습니다. 메모를 먼저 생성해주세요");
        }
    }

    @Transactional
    public void deleteMemo(Long memoId, Authentication auth) {
        // 메모 조회 및 존재하지 않을 경우 예외 처리
        GroupMemo groupMemo = groupMemoRepository.findById(memoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "일치하는 memo_id가 없음"));

        // ListGroup 조회
        ListGroup listGroup = groupMemo.getGroup();
        // 유저 권한 확인 절차
        if (!authCheck.hasAccessToCheckList(listGroup.getList().getListId(), auth)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "체크리스트에 접근할 수 없습니다.");
        }
        // 메모 삭제
        groupMemoRepository.delete(groupMemo); // 엔티티 삭제
        listGroup.setGroupMemo(null);
    }
}
