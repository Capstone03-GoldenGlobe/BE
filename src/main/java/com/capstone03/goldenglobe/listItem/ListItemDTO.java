package com.capstone03.goldenglobe.listItem;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ListItemDTO {
    private Long item_id;
    private Long user_id;
    private String item_name;
    private boolean ischecked;
    private Long group_id;

    // 생성자
    public static ListItemDTO fromEntity(ListItem listItem) {
        return new ListItemDTO(
                listItem.getItemId(),
                listItem.getUser() != null ? listItem.getUser().getUserId() : null,
                listItem.getItem(),
                listItem.isChecked(),
                listItem.getGroup().getGroupId()
        );
    }
}
