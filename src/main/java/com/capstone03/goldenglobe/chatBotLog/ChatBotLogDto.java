package com.capstone03.goldenglobe.chatBotLog;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatBotLogDto {

  private Long logId;
  private LocalDateTime chatDate;
  private String qna;
  private String content;
}
