package com.capstone03.goldenglobe.sharedList;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SharedListResponseDTO {

    private Long shared_id;
    private Long list_id;
    private Long user_id;
    private String user_nickname;
    private String user_color;
    private String user_profile;

    public static SharedListResponseDTO fromEntity(SharedList sharedList) {
        return new SharedListResponseDTO(
                sharedList.getSharedId(),
                sharedList.getList().getListId(),
                sharedList.getUser().getUserId(),
                sharedList.getUser().getNickname(),
                sharedList.getUserColor(),
                sharedList.getUser().getProfile()
        );
    }
}
