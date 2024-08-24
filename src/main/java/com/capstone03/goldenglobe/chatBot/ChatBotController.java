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

  @GetMapping("/{place_id}")
  public ResponseEntity<List<ChatBot>> getChatBotsByPlaceId(@PathVariable("place_id") String placeId) {
    List<ChatBot> chatBots = chatBotService.getChatBotsByPlaceId(placeId);
    return ResponseEntity.ok(chatBots);
  }

  @GetMapping("/{place_id}/pdflist")
  public ResponseEntity<List<ChatBot>> getPdfListByPlaceId(@PathVariable("place_id") String placeId) {
    // 나중에 PDF 조회 로직 추가하기
    List<ChatBot> chatBots = chatBotService.getChatBotsByPlaceId(placeId);
    return ResponseEntity.ok(chatBots);
  }

  @GetMapping
  public ResponseEntity<List<ChatBot>> getAllChatBots() {
    List<ChatBot> chatBots = chatBotService.getAllChatBots();
    return ResponseEntity.ok(chatBots);
  }
}
