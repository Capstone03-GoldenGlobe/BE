package com.capstone03.goldenglobe.chatBotLog;

import com.capstone03.goldenglobe.chatBot.ChatBot;
import com.capstone03.goldenglobe.chatBot.ChatBotRepository;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatBotLogService {

  @Autowired
  private ChatBotLogRepository chatBotLogRepository;

  @Autowired
  private ChatBotRepository chatBotRepository;

  // 새로운 질문과 답변 로그를 저장하는 메서드
  public void saveChatBotLog(Long destId, String qna, String content) {
    // dest_id로 ChatBot 엔티티 조회
    ChatBot chatBot = chatBotRepository.findByDestId(destId);
    if (chatBot == null) {
      // 해당 dest_id에 대한 새로운 채팅 세션을 생성
      chatBot = new ChatBot();
      chatBot.setDestId(destId);
      chatBotRepository.save(chatBot);
    }

    ChatBotLog chatBotLog = new ChatBotLog();
    chatBotLog.setChatId(chatBot.getChatId());
    chatBotLog.setChatDate(LocalDateTime.now());
    chatBotLog.setQna(qna);
    chatBotLog.setContent(content);
    chatBotLogRepository.save(chatBotLog);
  }

  // 특정 destId에 해당하는 채팅방의 로그를 조회하는 메서드
  public List<ChatBotLog> getLogsByDestId(Long destId) {
    ChatBot chatBot = chatBotRepository.findByDestId(destId);
    if (chatBot != null) {
      return chatBotLogRepository.findByChatId(chatBot.getChatId());
    } else {
      return null;  // 채팅방이 존재하지 않는 경우
    }
  }
}
