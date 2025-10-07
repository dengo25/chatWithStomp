package org.dengo.chat_backend.chat.repository;

import org.dengo.chat_backend.chat.domain.ChatRoom;
import org.dengo.chat_backend.chat.domain.ReadStatus;
import org.dengo.chat_backend.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, Long> {
  
  Long countByChatRoomAndMemberAndIsReadFalse(ChatRoom chatRoom, Member member);
  
  List<ReadStatus> findByChatRoomAndMember(ChatRoom chatRoom, Member member);
}
