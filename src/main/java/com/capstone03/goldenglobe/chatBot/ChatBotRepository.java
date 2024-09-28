package com.capstone03.goldenglobe.chatBot;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatBotRepository extends JpaRepository<ChatBot, Long> {
  List<ChatBot> findByDestId(Long destId);
}
