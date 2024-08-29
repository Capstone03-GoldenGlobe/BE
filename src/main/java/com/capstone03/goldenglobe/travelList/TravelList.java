package com.capstone03.goldenglobe.travelList;

import com.capstone03.goldenglobe.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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
  private User user;

  @Column(name = "country", length = 20)
  private String country;

  @Column(name = "city", length = 20)
  private String city;

  @Column(name = "start_date")
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;

}
