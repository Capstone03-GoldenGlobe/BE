package com.capstone03.goldenglobe;

import com.capstone03.goldenglobe.checkList.CheckList;
import com.capstone03.goldenglobe.checkList.CheckListRepository;
import com.capstone03.goldenglobe.sharedList.SharedListRepository;
import com.capstone03.goldenglobe.user.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckListAuthCheck {

    private final CheckListRepository checkListRepository;
    private final SharedListRepository sharedListRepository;

    public boolean hasAccessToCheckList(Long checkListId, Authentication auth) {
        // CheckList 존재 여부 확인
        CheckList checkList = checkListRepository.findByListId(checkListId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "체크리스트를 찾을 수 없습니다."));

        // 유저 권한 확인
        CustomUser customUser = (CustomUser) auth.getPrincipal();
        Long authUserId = customUser.getId();

        // 체크리스트 소유자 확인
        if (checkList.getUser().getUserId().equals(authUserId)) {
            return true;
        }

        // 체크리스트 공유 여부 확인
        return sharedListRepository.existsByList_ListIdAndUser_UserId(checkList.getListId(), authUserId);
    }
}