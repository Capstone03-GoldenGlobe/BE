package com.capstone03.goldenglobe.checklist;

import com.capstone03.goldenglobe.travellist.TravelList;
import com.capstone03.goldenglobe.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
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
