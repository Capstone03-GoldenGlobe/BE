package com.capstone03.goldenglobe;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="CheckList")
@Getter
@Setter
public class CheckList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="list_id",nullable = false)
    private Long listId;

    @ManyToOne
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name="dest_id",nullable = false, unique = true)
    private TravelList dest;

}
