package com.capstone03.goldenglobe.chatBot;

import com.capstone03.goldenglobe.travelList.TravelList;
import com.capstone03.goldenglobe.travelList.TravelListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/chat")
public class ChatBotController {

  private final ChatBotService chatBotService;
  private final TravelListRepository travelListRepository;

  @Autowired
  public ChatBotController(ChatBotService chatBotService, TravelListRepository travelListRepository) {
    this.chatBotService = chatBotService;
    this.travelListRepository = travelListRepository;
  }

  @PostMapping("/{place_id}")
  public ResponseEntity<ChatBot> createChatBot(@PathVariable("place_id") Long placeId, @RequestBody ChatBot chatBot) {
    Optional<TravelList> travelList = travelListRepository.findById(placeId);
    if (travelList.isPresent()) {
      chatBot.setDest(travelList.get()); // place_id를 destId로 설정
      ChatBot createdChatBot = chatBotService.createChatBot(chatBot);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdChatBot); // HTTP 201 Created
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // placeId에 해당하는 TravelList가 없을 경우
    }
  }

  @GetMapping("/{place_id}/pdflist")
  public ResponseEntity<List<ChatBot>> getPdfListByPlaceId(@PathVariable("place_id") Long placeId) {
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
