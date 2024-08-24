package com.capstone03.goldenglobe.travelList;

import com.capstone03.goldenglobe.user.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Table
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

//  @Column(name = "barrier_free")
//  private Boolean barrierFree;

  public Long getDestId() {
    return destId;
  }

  public void setDestId(Long destId) {
    this.destId = destId;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

}
