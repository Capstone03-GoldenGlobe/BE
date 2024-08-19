package com.capstone03.goldenglobe.listitem;

import com.capstone03.goldenglobe.groupmemo.GroupMemo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ListItemRepository extends JpaRepository<ListItem, Long> {
    List<ListItem> findByGroup_GroupId(Long groupId);
}
