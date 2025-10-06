package org.dengo.chat_backend.chat.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dengo.chat_backend.member.domain.Member;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ChatMessage {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  private ChatRoom chatRoom;
  
  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;
  
  private String content;
  
}
