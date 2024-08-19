package com.capstone03.goldenglobe.groupMemo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupMemoRepository extends JpaRepository<GroupMemo, Long> {
    Optional<GroupMemo> findByGroup_GroupId(Long listGroupId);
}
