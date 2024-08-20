package com.capstone03.goldenglobe.listGroup;

import com.capstone03.goldenglobe.checkList.CheckList;
import com.capstone03.goldenglobe.groupMemo.GroupMemo;
import com.capstone03.goldenglobe.listItem.ListItem;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table
@Getter
@Setter
public class ListGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="group_id",nullable = false)
    private Long groupId;

    @ManyToOne
    @JoinColumn(name="list_id",nullable = false)
    private CheckList list;

    @Column(name="group_name",nullable = false)
    private String groupName;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ListItem> listItems;

    @OneToOne(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private GroupMemo groupMemo;
}
