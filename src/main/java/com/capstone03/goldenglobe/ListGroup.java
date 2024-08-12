package com.capstone03.goldenglobe;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="ListGroup")
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
}
