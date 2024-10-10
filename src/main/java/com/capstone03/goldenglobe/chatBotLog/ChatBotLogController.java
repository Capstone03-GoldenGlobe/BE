package com.capstone03.goldenglobe.chatBotLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chatbot/log")
public class ChatBotLogController {

  @Autowired
  private ChatBotLogService chatBotLogService;

  @GetMapping("/{chat_id}")
  public ResponseEntity<List<ChatBotLogDto>> getLogsByChatId(@PathVariable("chat_id") Long chatId) {
    List<ChatBotLogDto> logs = chatBotLogService.getLogsByChatId(chatId);
    if (logs.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(logs);
  }
}
