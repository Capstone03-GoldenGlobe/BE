package com.capstone03.goldenglobe.listitem;

import com.capstone03.goldenglobe.User;
import com.capstone03.goldenglobe.checklist.CheckList;
import com.capstone03.goldenglobe.listgroup.ListGroup;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table
@Getter
@Setter
public class ListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="item_id",nullable = false)
    private Long itemId;

    @ManyToOne
    @JoinColumn(name="list_id",nullable = false)
    private CheckList list;

    @ManyToOne
    @JoinColumn(name="group_id",nullable = false)
    private ListGroup group;

    @ManyToOne
    @JoinColumn(name="user_id") // 체크한 유저
    private User user;

    @Column(name="item")
    private String item;

    @ColumnDefault("0") // False
    @Column(name = "is_checked", nullable = false)
    private boolean isChecked;
}
