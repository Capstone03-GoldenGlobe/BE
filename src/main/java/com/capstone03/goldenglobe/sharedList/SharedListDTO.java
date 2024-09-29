package com.capstone03.goldenglobe.sharedList;

import com.capstone03.goldenglobe.groupMemo.GroupMemo;
import com.capstone03.goldenglobe.groupMemo.GroupMemoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SharedListDTO {

    private Long shared_id;
    private Long list_id;
    private Long user_id;
    private String user_nickname;
    private String user_color;
    private String user_profile;

    public static SharedListDTO fromEntity(SharedList sharedList) {
        return new SharedListDTO(
                sharedList.getSharedId(),
                sharedList.getList().getListId(),
                sharedList.getUser().getUserId(),
                sharedList.getUser().getNickname(),
                sharedList.getUserColor(),
                sharedList.getUser().getProfile()
        );
    }
}
