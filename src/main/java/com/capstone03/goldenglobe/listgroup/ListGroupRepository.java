package com.capstone03.goldenglobe.listgroup;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListGroupRepository extends JpaRepository<ListGroup, Long> {
    List<ListGroup> findByList_ListId(Long checkListId);
}
