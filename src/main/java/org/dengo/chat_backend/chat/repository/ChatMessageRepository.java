package org.dengo.chat_backend.chat.repository;

import org.dengo.chat_backend.chat.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

}
