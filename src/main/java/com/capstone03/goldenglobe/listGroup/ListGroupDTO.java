package com.capstone03.goldenglobe.listGroup;

import com.capstone03.goldenglobe.listItem.ListItem;
import com.capstone03.goldenglobe.listItem.ListItemDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class ListGroupDTO {

    private Long group_id;
    private String group_name;
    private Long list_id;

    // 생성자
    public static ListGroupDTO fromEntity(ListGroup listGroup) {
        return new ListGroupDTO(
                listGroup.getGroupId(),
                listGroup.getGroupName(),
                listGroup.getList().getListId()
        );
    }
}
