package com.capstone03.goldenglobe.groupmemo;

import com.capstone03.goldenglobe.listgroup.ListGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

public interface GroupMemoRepository extends JpaRepository<GroupMemo, Long> {
    Optional<GroupMemo> findByGroup_GroupId(Long listGroupId);
}
