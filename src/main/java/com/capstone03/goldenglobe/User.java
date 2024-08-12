package com.capstone03.goldenglobe;
import jakarta.persistence.*;

import java.util.List;
import java.time.LocalDateTime;

@Entity
@Table
public class User {
  @Id
  @Column(name = "user_id", nullable = false, length = 20)
  private String userId;

  @Column(name = "name", nullable = false, length = 20)
  private String name;

  @Column(name = "birth")
  private LocalDateTime birth;

  @Column(name = "cellphone", length = 20)
  private String cellphone;

  @Column(name = "email", length = 30)
  private String email;

  @Column(name = "id", nullable = false, length = 20)
  private String id;

  @Column(name = "password", nullable = false, length = 20)
  private String password;

  @Column(name = "nikname", length = 10)
  private String nickname;

  @Column(name = "profile", length = 30)
  private String profile;

  @Column(name = "gender", length = 10)
  private String gender;

  @OneToMany(mappedBy = "user")
  private List<TravelList> travelLists;
}
