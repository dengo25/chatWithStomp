package org.dengo.chat_backend.chat.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dengo.chat_backend.member.domain.Member;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReadStatus {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "chat_room_id", nullable = false)
  public ChatRoom chatRoom;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  public Member member;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "chat_message_id", nullable = false)
  private ChatMessage chatMessage;
  
  @Column(nullable = false)
  private Boolean isRead;
  
  public void updateIsRead(boolean isRead) {
    this.isRead = isRead;
  }
}
