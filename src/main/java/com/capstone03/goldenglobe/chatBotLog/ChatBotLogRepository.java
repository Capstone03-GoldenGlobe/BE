package com.capstone03.goldenglobe.chatBotLog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatBotLogRepository extends JpaRepository<ChatBotLog, Long> {
  List<ChatBotLog> findByChatId(Long chatId);
}
