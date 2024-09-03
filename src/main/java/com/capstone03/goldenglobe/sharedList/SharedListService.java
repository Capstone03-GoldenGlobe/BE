package com.capstone03.goldenglobe.sharedList;

import com.capstone03.goldenglobe.CheckListAuthCheck;
import com.capstone03.goldenglobe.checkList.CheckList;
import com.capstone03.goldenglobe.checkList.CheckListRepository;
import com.capstone03.goldenglobe.user.CustomUser;
import com.capstone03.goldenglobe.user.User;
import com.capstone03.goldenglobe.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class SharedListService {
    private final SharedListRepository sharedListRepository;
    private final CheckListRepository checkListRepository;
    private final UserRepository userRepository;
    private final CheckListAuthCheck authCheck;

    public SharedList addUser(Long list_id, Long user_id, Authentication auth){
        // 일치하는 체크리스트가 있는지 확인
        CheckList checkList = authCheck.findAndCheckAccessToList(list_id,auth);

        SharedList sharedList = new SharedList();
        sharedList.setList(checkList);

        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 user_id가 없음"));
        sharedList.setUser(user);

        return sharedListRepository.save(sharedList);
    }

    public SharedList changeColor(Long list_id, String user_color, Authentication auth){
        // 체크리스트 접근 권한 확인
        authCheck.findAndCheckAccessToList(list_id, auth);

        // 현재 인증된 유저의 이메일을 가져와서 유저 아이디 조회
        CustomUser customUser = (CustomUser) auth.getPrincipal();
        User user = userRepository.findByCellphone(customUser.getCellphone())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));

        // listId와 userId로 SharedList 조회
        SharedList sharedList = sharedListRepository.findByList_ListIdAndUser_UserId(list_id, user.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SharedList를 찾을 수 없습니다."));

        // 색상 변경
        sharedList.setUserColor(user_color);

        // 변경된 SharedList 저장
        return sharedListRepository.save(sharedList);
    }

    public void deleteShare(Long list_id, Authentication auth) {
        //list_id 로 sharedList 찾고, 그중 auth의 id와 일치하는 user_id가 있는 것 삭제
        SharedList sharedList = authCheck.findSharedListByListIdAndAuth(list_id,auth);
        // 아이템 삭제
        sharedListRepository.delete(sharedList);
    }
}
