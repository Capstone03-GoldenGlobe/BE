package com.capstone03.goldenglobe.groupMemo;

import com.capstone03.goldenglobe.listGroup.ListGroup;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
public class GroupMemo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="memo_id",nullable = false)
    private Long memoId;

    @OneToOne
    @JoinColumn(name="group_id",nullable = false,unique = true)
    private ListGroup group; // unique

    @Column(name="memo")
    private String memo;

}
