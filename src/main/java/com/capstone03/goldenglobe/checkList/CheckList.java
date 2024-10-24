package com.capstone03.goldenglobe.checkList;

import com.capstone03.goldenglobe.listGroup.ListGroup;
import com.capstone03.goldenglobe.sharedList.SharedList;
import com.capstone03.goldenglobe.travelList.TravelList;
import com.capstone03.goldenglobe.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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

    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ListGroup> listGroups;

    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SharedList> sharedLists;
}
