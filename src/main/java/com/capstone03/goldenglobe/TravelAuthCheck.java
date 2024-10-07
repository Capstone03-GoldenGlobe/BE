package com.capstone03.goldenglobe;

import com.capstone03.goldenglobe.checkList.CheckList;
import com.capstone03.goldenglobe.sharedList.SharedListRepository;
import com.capstone03.goldenglobe.travelList.TravelList;
import com.capstone03.goldenglobe.travelList.TravelListRepository;
import com.capstone03.goldenglobe.user.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TravelAuthCheck {
    private final TravelListRepository travelListRepository;
    private final SharedListRepository sharedListRepository;

    public TravelList isOkay(long destId, Authentication auth){
        // destId 존재 여부 확인
        TravelList travelList = travelListRepository.findByDestId(destId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "여행지를 찾을 수 없습니다."));

        // 유저 권한 확인
        CustomUser customUser = (CustomUser) auth.getPrincipal();
        Long authUserId = customUser.getId();

        // 여행지 소유자 확인 또는 공유 목록 확인
        if (travelList.getUser().getUserId().equals(authUserId) ||
                sharedListRepository.existsByList_ListIdAndUser_UserId(destId, authUserId)) {
            return travelList;
        }

        // 권한 없음
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.");
    }
}
