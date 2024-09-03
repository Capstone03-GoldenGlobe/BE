package com.capstone03.goldenglobe.groupMemo;

import com.capstone03.goldenglobe.listGroup.ListGroup;
import com.capstone03.goldenglobe.listGroup.ListGroupDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupMemoDTO {

    private Long group_id;
    private Long memo_id;
    private String memo;

    // 생성자
    public static GroupMemoDTO fromEntity(GroupMemo groupMemo) {
        return new GroupMemoDTO(
                groupMemo.getGroup().getGroupId(),
                groupMemo.getMemoId(),
                groupMemo.getMemo()
        );
    }
}
