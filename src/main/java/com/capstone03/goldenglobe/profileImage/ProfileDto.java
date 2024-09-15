package com.capstone03.goldenglobe.profileImage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {
    private String presignedUrl;
    private String profileUrl;
}
