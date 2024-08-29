package com.capstone03.goldenglobe.sharedList;

import com.capstone03.goldenglobe.CheckListAuthCheck;
import com.capstone03.goldenglobe.checkList.CheckList;
import com.capstone03.goldenglobe.checkList.CheckListRepository;
import com.capstone03.goldenglobe.listGroup.ListGroup;
import com.capstone03.goldenglobe.user.User;
import com.capstone03.goldenglobe.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Check;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

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
}
