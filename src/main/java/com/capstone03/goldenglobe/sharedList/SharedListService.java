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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SharedListService {
    private final SharedListRepository sharedListRepository;
    private final CheckListRepository checkListRepository;
    private final UserRepository userRepository;
    private final CheckListAuthCheck authCheck;

    public SharedList addUser(Long listId, Long userId, Authentication auth){
        // 일치하는 체크리스트가 있는지 확인
        CheckList checkList = authCheck.findAndCheckAccessToList(listId,auth);

        SharedList sharedList = new SharedList();
        sharedList.setList(checkList);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "일치하는 user_id가 없음"));
        sharedList.setUser(user);

        return sharedListRepository.save(sharedList);
    }

    public SharedList changeColor(Long listId, String userColor, Authentication auth){
        // 체크리스트 접근 권한 확인
        authCheck.findAndCheckAccessToList(listId, auth);

        // 현재 인증된 유저의 이메일을 가져와서 유저 아이디 조회
        CustomUser customUser = (CustomUser) auth.getPrincipal();
        User user = userRepository.findByCellphone(customUser.getCellphone())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));

        // listId와 userId로 SharedList 조회
        SharedList sharedList = sharedListRepository.findByList_ListIdAndUser_UserId(listId, user.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SharedList를 찾을 수 없습니다."));

        // 색상 변경
        sharedList.setUserColor(userColor);

        // 변경된 SharedList 저장
        return sharedListRepository.save(sharedList);
    }

    public void deleteShare(Long listId, Authentication auth) {
        //list_id 로 sharedList 찾고, 그중 auth의 id와 일치하는 user_id가 있는 것 삭제
        SharedList sharedList = authCheck.findSharedListByListIdAndAuth(listId,auth);
        // 아이템 삭제
        sharedListRepository.delete(sharedList);
    }

    public void deleteShareOwner(Long listId, Long userId, Authentication auth){
        Boolean canCheck = authCheck.isOwner(listId, auth);
        if (canCheck) {
            Optional<SharedList> sharedList = sharedListRepository.findByList_ListIdAndUser_UserId(listId, userId);
            sharedList.ifPresent(sharedListRepository::delete);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "공유 해제 권한이 없습니다. 체크리스트의 소유자가 아닙니다.");
        }
    }
}
