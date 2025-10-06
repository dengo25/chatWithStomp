package org.dengo.chat_backend.chat.repository;

import org.dengo.chat_backend.chat.domain.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {

}
