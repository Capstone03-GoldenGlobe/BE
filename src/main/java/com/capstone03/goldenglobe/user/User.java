package com.capstone03.goldenglobe.user;
import com.capstone03.goldenglobe.travelList.TravelList;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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

  @Column(name = "cellphone", length = 11) // unique 제약조건추가
  private String cellphone;

  @Column(name = "password", nullable = false, columnDefinition = "TEXT")
  private String password;

  @Column(name = "nickname", length = 10, unique = true)
  private String nickname;

  @Column(name = "profile")
  private String profile;

  @Column(name = "gender", length = 10)
  private String gender;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TravelList> travelLists;

  @ElementCollection(fetch = FetchType.EAGER)  // 권한을 EAGER 로딩
  @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
  @Column(name = "role")
  private Set<String> roles;
}
