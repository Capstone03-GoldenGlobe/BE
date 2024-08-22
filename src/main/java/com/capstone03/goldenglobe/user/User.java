package com.capstone03.goldenglobe.user;
import com.capstone03.goldenglobe.travelList.TravelList;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table
@Getter
@Setter
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id", nullable = false, length = 20)
  private Long userId;

  @Column(name = "name", nullable = false, length = 20)
  private String name;

  @Column(name = "birth")
  private LocalDate birth;

  @Column(name = "cellphone", length = 20)
  private String cellphone;

  @Column(name = "email", length = 30)
  private String email;

  @Column(name = "password", nullable = false, columnDefinition = "TEXT")
  private String password;

  @Column(name = "nickname", length = 10)
  private String nickname;

  @Column(name = "profile", length = 30)
  private String profile;

  @Column(name = "gender", length = 10)
  private String gender;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TravelList> travelLists;
}
