package com.capstone03.goldenglobe.chatBotLog;

import lombok.Data;

@Data
public class ChatBotLogRequest {
  private Long destId;  // dest_id를 기준으로 채팅방 구분
  private String question;
  private String answer;
}
