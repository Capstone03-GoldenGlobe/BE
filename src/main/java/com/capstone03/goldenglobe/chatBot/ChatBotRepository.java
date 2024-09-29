package com.capstone03.goldenglobe.chatBot;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatBotRepository extends JpaRepository<ChatBot, Long> {
  ChatBot findByDestId(Long destId);  // 단일 ChatBot 객체를 반환
}
