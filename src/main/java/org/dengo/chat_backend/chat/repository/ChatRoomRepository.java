package org.dengo.chat_backend.chat.repository;

import org.dengo.chat_backend.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

}
