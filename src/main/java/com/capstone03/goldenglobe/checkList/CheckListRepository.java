package com.capstone03.goldenglobe.checkList;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CheckListRepository extends JpaRepository<CheckList, Long> {
    // listId로 체크리스트 찾기
    Optional<CheckList> findByListId(Long listId);

    // destId로 체크리스트 찾기
    Optional<CheckList> findByDest_DestId(Long destId);
}
