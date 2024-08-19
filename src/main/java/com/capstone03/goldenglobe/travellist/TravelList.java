package com.capstone03.goldenglobe.travellist;

import com.capstone03.goldenglobe.User;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table
public class TravelList {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "dest_id", nullable = false, length = 20)
  private String destId;

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

  @Column(name = "barrier_free")
  private Boolean barrierFree;
}
