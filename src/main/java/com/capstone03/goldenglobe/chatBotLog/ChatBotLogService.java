package com.capstone03.goldenglobe.chatBotLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatBotLogService {

  @Autowired
  private ChatBotLogRepository chatBotLogRepository;

  public List<ChatBotLogDto> getLogsByChatId(Long chatId) {
    List<ChatBotLog> logs = chatBotLogRepository.findByChatId(chatId);
    return logs.stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

  private ChatBotLogDto convertToDto(ChatBotLog log) {
    ChatBotLogDto dto = new ChatBotLogDto();
    dto.setLogId(log.getLogId());
    dto.setChatDate(log.getChatDate());
    dto.setQna(log.getQna());
    dto.setContent(log.getContent());
    return dto;
  }
}
