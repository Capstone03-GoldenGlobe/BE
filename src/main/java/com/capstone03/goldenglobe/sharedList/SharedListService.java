package com.capstone03.goldenglobe.sharedList;

import com.capstone03.goldenglobe.CheckListAuthCheck;
import com.capstone03.goldenglobe.checkList.CheckList;
import com.capstone03.goldenglobe.checkList.CheckListRepository;
import com.capstone03.goldenglobe.profileImage.ProfileService;
import com.capstone03.goldenglobe.user.CustomUser;
import com.capstone03.goldenglobe.user.User;
import com.capstone03.goldenglobe.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SharedListService {
    private final SharedListRepository sharedListRepository;
    private final UserRepository userRepository;
    private final CheckListAuthCheck authCheck;
    private final ProfileService profileService;

    public SharedList addUser(Long listId, String cellPhone, Authentication auth) {
        // 일치하는 체크리스트가 있는지 확인
        CheckList checkList = authCheck.findAndCheckAccessToList(listId, auth);

        // cellPhone
        User user = userRepository.findByCellphone(cellPhone)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));

        // sharedList의 setList, setUser가 같은 경우 추가 X
        if (sharedListRepository.existsByListAndUser(checkList, user)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "유저가 이미 공유된 체크리스트에 추가되었습니다.");
        }

        SharedList sharedList = new SharedList();
        sharedList.setList(checkList);
        sharedList.setUser(user);

        return sharedListRepository.save(sharedList);
    }

    public List<SharedListDTO> getSharedUsers(Long listId, Authentication auth){
        // list_id를 토대로 해당 리스트에 접근 권한이 있는지 확인
        CheckList checkList = authCheck.findAndCheckAccessToList(listId, auth);

        // 해당 체크리스트를 공유받은 사용자 목록 조회
        List<SharedList> sharedLists = sharedListRepository.findByList_ListId(checkList.getListId());

        // DTO로 변환하여 반환할 데이터 준비
        return sharedLists.stream()
                .map(sharedList -> new SharedListDTO(
                        sharedList.getSharedId(),
                        sharedList.getList().getListId(),
                        sharedList.getUser().getUserId(),
                        sharedList.getUser().getNickname(),
                        sharedList.getUserColor(),
                        profileService.getProfileUrl(sharedList.getUser())
                ))
                .collect(Collectors.toList());
    }

    public SharedList changeColor(Long listId, String userColor, Authentication auth) {
        // 체크리스트 접근 권한 확인
        authCheck.findAndCheckAccessToList(listId, auth);

        // 현재 인증된 유저의 이메일을 가져와서 유저 아이디 조회
        CustomUser customUser = (CustomUser) auth.getPrincipal();
        User user = userRepository.findByCellphone(customUser.getCellphone())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));

        // listId와 userId로 SharedList 조회
        SharedList sharedList = sharedListRepository.findByList_ListIdAndUser_UserId(listId, user.getUserId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SharedList를 찾을 수 없습니다."));

        // userColor가 헥스코드(6자리)인지 확인
        if (!userColor.matches("^#[0-9A-Fa-f]{6}$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "6자리 헥스코드로 입력해주세요.(예: #ff0099)");
        }

        // 색상 변경
        sharedList.setUserColor(userColor);

        // 변경된 SharedList 저장
        return sharedListRepository.save(sharedList);
    }

    public void deleteShare(Long listId, Authentication auth) {
        //list_id 로 sharedList 찾고, 그중 auth의 id와 일치하는 user_id가 있는 것 삭제
        SharedList sharedList = authCheck.findSharedListByListIdAndAuth(listId, auth);
        // 아이템 삭제
        sharedListRepository.delete(sharedList);
    }

    public void deleteShareOwner(Long listId, Long userId, Authentication auth) {
        Boolean canCheck = authCheck.isOwner(listId, auth);
        if (canCheck) {
            SharedList sharedList = sharedListRepository.findByList_ListIdAndUser_UserId(listId, userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "공유된 사용자를 찾을 수 없습니다."));
            sharedListRepository.delete(sharedList); // 삭제
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "공유 해제 권한이 없습니다. 체크리스트의 소유자가 아닙니다.");
        }
    }
}
