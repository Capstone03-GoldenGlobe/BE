package com.capstone03.goldenglobe.checkList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponseDTO {
    private String itemId;
    private String itemName;
    private boolean check;
    private String userId;
}
