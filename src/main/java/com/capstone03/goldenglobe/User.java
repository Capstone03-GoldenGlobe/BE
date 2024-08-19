package com.capstone03.goldenglobe;
import com.capstone03.goldenglobe.travellist.TravelList;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id", nullable = false, length = 20)
  private String userId;

  @Column(name = "name", nullable = false, length = 20)
  private String name;

  @Column(name = "birth")
  private LocalDate birth;

  @Column(name = "cellphone", length = 20)
  private String cellphone;

  @Column(name = "email", length = 30)
  private String email;

  @Column(name = "password", nullable = false, length = 20)
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
