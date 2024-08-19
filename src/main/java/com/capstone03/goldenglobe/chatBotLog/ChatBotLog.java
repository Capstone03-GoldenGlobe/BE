package com.capstone03.goldenglobe.chatBotLog;

import com.capstone03.goldenglobe.chatBot.ChatBot;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class ChatBotLog {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "log_id", nullable = false, length = 50)
  private Long logId;

  @Column(name = "chat_id", nullable = false, length = 50)
  private Long chatId;

  @Column(name = "chat_date", nullable = false)
  private LocalDateTime chatDate;

  @Column(name = "qna", nullable = false, length = 10)
  private String qna;

  @Column(name = "content", nullable = false, length = 300)
  private String content;

  @ManyToOne
  @JoinColumn(name = "chat_id", insertable = false, updatable = false)
  private ChatBot chatBot;
}
