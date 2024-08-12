package com.capstone03.goldenglobe;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="TravelList")
public class TravelList {
  @Id
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
  private LocalDateTime startDate;

  @Column(name = "end_date")
  private LocalDateTime endDate;

  @Column(name = "barrier_free")
  private Boolean barrierFree;
}
