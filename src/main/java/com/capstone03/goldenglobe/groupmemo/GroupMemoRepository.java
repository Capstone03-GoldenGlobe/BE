package com.capstone03.goldenglobe.groupmemo;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;

public interface GroupMemoRepository extends JpaRepository<GroupMemo, Long> {
}
