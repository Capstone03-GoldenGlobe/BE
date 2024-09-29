package com.capstone03.goldenglobe.sharedList;

import com.capstone03.goldenglobe.checkList.CheckList;
import com.capstone03.goldenglobe.user.CustomUser;
import com.capstone03.goldenglobe.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SharedListRepository extends JpaRepository<SharedList, Long> {
    boolean existsByList_ListIdAndUser_UserId(Long listId, Long userId);
    Optional<SharedList> findByList_ListIdAndUser_UserId(Long listId, Long userId);
    List<SharedList> findByUser_UserId(Long userId);
    boolean existsByListAndUser(CheckList checkList, User user);
}
