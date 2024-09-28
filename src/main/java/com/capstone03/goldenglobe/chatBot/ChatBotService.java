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

  public List<ChatBot> getChatBotsByPlaceId(Long placeId) {
    return chatBotRepository.findByDestId(placeId);
  }

  public List<ChatBot> getAllChatBots() {
    return chatBotRepository.findAll();
  }

  public ChatBot createChatBot(ChatBot chatBot) {
    return chatBotRepository.save(chatBot);
  }
}
