package org.dengo.chat_backend.chat.repository;

import org.dengo.chat_backend.chat.domain.ChatMessage;
import org.dengo.chat_backend.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
  
  List<ChatMessage> findByChatRoomOrderByCreatedTimeAsc(ChatRoom chatRoom);
}
