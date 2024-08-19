package com.capstone03.goldenglobe.listgroup;

import com.capstone03.goldenglobe.listitem.ListItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ListGroupRepository extends JpaRepository<ListGroup, Long> {
    List<ListGroup> findByList_ListId(Long checkListId);

    Optional<ListGroup> findByGroupId(Long groupId);
}
