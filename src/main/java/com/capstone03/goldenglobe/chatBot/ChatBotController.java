package com.capstone03.goldenglobe.chatBot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatBotController {

  private final ChatBotService chatBotService;

  @Autowired
  public ChatBotController(ChatBotService chatBotService) {
    this.chatBotService = chatBotService;
  }

  // 새로운 ChatBot 생성
  @PostMapping("/{place_id}")
  public ResponseEntity<ChatBot> createChatBot(@PathVariable("place_id") Long placeId, @RequestBody ChatBot chatBot) {
    chatBot.setDestId(placeId); // place_id를 destId로 설정
    ChatBot createdChatBot = chatBotService.createChatBot(chatBot);
    return ResponseEntity.status(201).body(createdChatBot); // HTTP 201 Created
  }

  // 단일 ChatBot 조회 (다중이 아닌 경우)
  @GetMapping("/{place_id}/pdflist")
  public ResponseEntity<ChatBot> getPdfListByPlaceId(@PathVariable("place_id") Long placeId) {
    // place_id로 단일 ChatBot 반환
    ChatBot chatBot = chatBotService.getChatBotByPlaceId(placeId);
    return ResponseEntity.ok(chatBot);
  }

  // 모든 ChatBot 조회
  @GetMapping
  public ResponseEntity<List<ChatBot>> getAllChatBots() {
    List<ChatBot> chatBots = chatBotService.getAllChatBots();
    return ResponseEntity.ok(chatBots);
  }
}
