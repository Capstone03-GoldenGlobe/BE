package com.capstone03.goldenglobe;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="SharedList")
@Getter
@Setter
public class SharedList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="shared_id")
    private Long sharedId;

    @ManyToOne
    @JoinColumn(name="list_id")
    private CheckList list;

    @ManyToMany
    @JoinColumn(name="user_id")
    private User user;

    @Column(name="user_color")
    private String userColor;

}
