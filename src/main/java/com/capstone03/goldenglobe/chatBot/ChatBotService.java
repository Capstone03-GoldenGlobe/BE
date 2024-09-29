package com.capstone03.goldenglobe.chatBot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatBotService {

  private final ChatBotRepository chatBotRepository;

  @Autowired
  public ChatBotService(ChatBotRepository chatBotRepository) {
    this.chatBotRepository = chatBotRepository;
  }

  // destId로 단일 ChatBot을 반환하도록 수정
  public ChatBot getChatBotByPlaceId(Long placeId) {
    return chatBotRepository.findByDestId(placeId);  // 수정된 부분
  }

  // 모든 ChatBot 반환
  public List<ChatBot> getAllChatBots() {
    return chatBotRepository.findAll();
  }

  // ChatBot 생성
  public ChatBot createChatBot(ChatBot chatBot) {
    return chatBotRepository.save(chatBot);
  }
}
