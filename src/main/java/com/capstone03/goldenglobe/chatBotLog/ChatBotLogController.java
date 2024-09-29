package com.capstone03.goldenglobe.chatBotLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatbot")
public class ChatBotLogController {

  @Autowired
  private ChatBotLogService chatBotLogService;

  // 새로운 질문과 답변 로그 저장
  @PostMapping("/save-log")
  public void saveChatBotLog(@RequestBody ChatBotLogRequest request) {
    chatBotLogService.saveChatBotLog(request.getDestId(), "Q", request.getQuestion());
    chatBotLogService.saveChatBotLog(request.getDestId(), "A", request.getAnswer());
  }

  // 특정 destId에 대한 채팅방의 로그 조회
  @GetMapping("/logs/{destId}")
  public List<ChatBotLog> getChatBotLogs(@PathVariable Long destId) {
    return chatBotLogService.getLogsByDestId(destId);
  }
}