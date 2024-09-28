package com.capstone03.goldenglobe.chatBot;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ChatBot {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "chat_id", nullable = false, length = 50)
  private Long chatId;

  @Column(name = "dest_id", nullable = false, length = 50)
  private Long destId;
}
