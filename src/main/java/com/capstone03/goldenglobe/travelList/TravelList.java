package com.capstone03.goldenglobe.travelList;

import com.capstone03.goldenglobe.chatBot.ChatBot;
import com.capstone03.goldenglobe.checkList.CheckList;
import com.capstone03.goldenglobe.groupMemo.GroupMemo;
import com.capstone03.goldenglobe.listItem.ListItem;
import com.capstone03.goldenglobe.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table
@Getter
@Setter
public class TravelList {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "dest_id", nullable = false, length = 20)
  private Long destId;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  @JsonIgnore
  private User user;

  @Column(name = "country", length = 20)
  private String country;

  @Column(name = "city", length = 20)
  private String city;

  @Column(name = "start_date")
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;

  @OneToOne(mappedBy = "dest", cascade = CascadeType.ALL, orphanRemoval = true)
  private CheckList checkList;

  @OneToOne(mappedBy = "dest", cascade = CascadeType.ALL, orphanRemoval = true)
  private ChatBot chatBot;
}
