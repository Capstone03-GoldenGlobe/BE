package com.capstone03.goldenglobe;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="GroupMemo")
@Getter
@Setter
public class GroupMemo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="memo_id",nullable = false)
    private Long memoId;

    @OneToOne
    @JoinColumn(name="group_id",nullable = false)
    private ListGroup group;

    @Column(name="memo")
    private String memo;

}
