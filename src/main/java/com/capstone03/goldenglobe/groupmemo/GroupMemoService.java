package com.capstone03.goldenglobe.groupmemo;

import com.capstone03.goldenglobe.listgroup.ListGroup;
import com.capstone03.goldenglobe.listgroup.ListGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupMemoService {
    private final GroupMemoRepository groupMemoRepository;
    private final ListGroupRepository listGroupRepository;

    public GroupMemo makeMemo(Long group_id, String memo){
        // 일치하는 그룹이 있는지 확인
        ListGroup listGroup = listGroupRepository.findById(group_id)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 group_id가 없음"));

        GroupMemo groupMemo = new GroupMemo();
        groupMemo.setGroup(listGroup);
        groupMemo.setMemo(memo);

        // 저장
        return groupMemoRepository.save(groupMemo);
    }
}
