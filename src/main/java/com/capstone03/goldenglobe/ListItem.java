package com.capstone03.goldenglobe;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name="ListItem")
@Getter
@Setter
public class ListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="item_id",nullable = false)
    private Long itemId;

    @ManyToOne
    @JoinColumn(name="list_id")
    private CheckList list;

    @ManyToOne
    @JoinColumn(name="group_id")
    private ListGroup group;

    @ManyToMany
    @JoinColumn(name="user_id")
    private User user;

    @Column(name="item")
    private String item;

    @ColumnDefault("false")
    private boolean check;
}
