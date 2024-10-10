package com.capstone03.goldenglobe.chatBotLog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatBotLogRepository extends JpaRepository<ChatBotLog, Long> {
  List<ChatBotLog> findByChatId(Long chatId);
}
