package com.capstone03.goldenglobe.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UserDTO {
    private Long userId;
    private String name;
    private LocalDate birth;
    private String cellphone;
    private String nickname;
    private String gender;

    // 생성자
    public static UserDTO fromEntity(User user) {
        return new UserDTO(
                user.getUserId(),
                user.getName(),
                user.getBirth(),
                user.getCellphone(),
                user.getNickname(),
                user.getGender()
        );
    }
}
