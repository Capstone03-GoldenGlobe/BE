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
public class CheckListResponseDTO {
    private String country;
    private String city;
    private String listId;
    private List<GroupResponseDTO> groups;
}

