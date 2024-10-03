package com.capstone03.goldenglobe.chatBot;

import com.capstone03.goldenglobe.travelList.TravelList;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ChatBot {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "chat_id", nullable = false)
  private Long chatId;

  @OneToOne
  @JoinColumn(name="dest_id",nullable = false, unique = true)
  private TravelList dest;
}
