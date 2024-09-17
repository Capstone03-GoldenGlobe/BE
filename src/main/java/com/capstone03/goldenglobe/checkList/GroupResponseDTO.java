package com.capstone03.goldenglobe.checkList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupResponseDTO {
    private String groupId;
    private String groupName;
    private String memoId;
    private String memo;
    private List<ItemResponseDTO> items;
}
