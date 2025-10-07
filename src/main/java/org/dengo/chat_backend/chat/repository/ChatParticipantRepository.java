package org.dengo.chat_backend.chat.repository;

import org.dengo.chat_backend.chat.domain.ChatParticipant;
import org.dengo.chat_backend.chat.domain.ChatRoom;
import org.dengo.chat_backend.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {
  
  @Query("select cp1.chatRoom from ChatParticipant cp1 " +
      " join ChatParticipant cp2 on cp1.chatRoom.id = cp2.chatRoom.id " +
      " where cp1.member.id = :myId and cp2.member.id = :otherMemberId and cp1.chatRoom.isGroupChat = 'N'")
  Optional<ChatRoom> findExistingPrivateRoom(@Param("myId") Long myId, @Param("otherMemberId") Long otherMemberId);
  
  List<ChatParticipant> findByChatRoom(ChatRoom chatRoom);
  
  List<ChatParticipant> findAllByMember(Member member);
}
