package com.capstone03.goldenglobe;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ChatBot {
  @Id
  @Column(name = "chat_id", nullable = false, length = 50)
  private String chatId;

  @Column(name = "dest_id", nullable = false, length = 50)
  private String destId;
}
