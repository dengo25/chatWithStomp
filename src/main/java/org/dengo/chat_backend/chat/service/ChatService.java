package org.dengo.chat_backend.chat.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.dengo.chat_backend.chat.domain.ChatMessage;
import org.dengo.chat_backend.chat.domain.ChatParticipant;
import org.dengo.chat_backend.chat.domain.ChatRoom;
import org.dengo.chat_backend.chat.domain.ReadStatus;
import org.dengo.chat_backend.chat.dto.ChatMessageDTO;
import org.dengo.chat_backend.chat.repository.ChatMessageRepository;
import org.dengo.chat_backend.chat.repository.ChatParticipantRepository;
import org.dengo.chat_backend.chat.repository.ChatRoomRepository;
import org.dengo.chat_backend.chat.repository.ReadStatusRepository;
import org.dengo.chat_backend.member.domain.Member;
import org.dengo.chat_backend.member.repository.MemberRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {
  
  private final ChatRoomRepository chatRoomRepository;
  private final ChatParticipantRepository chatParticipantRepository;
  private final ChatMessageRepository chatMessageRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MemberRepository memberRepository;
  
  public Long getOrCreatePrivateRoom(Long otherMemberId) {
    Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new EntityNotFoundException("채팅방을 찾을 수 없습니다"));
    Member otherMember = memberRepository.findById(otherMemberId).orElseThrow(() -> new EntityNotFoundException("member Not found"));
    
    // 방을 만들기전에 두사람이 속한 방이 있는지 확인
    Optional<ChatRoom> chatRoom = chatParticipantRepository.findExistingPrivateRoom(member.getId(), otherMember.getId());
    
    if (chatRoom.isPresent()) return chatRoom.get().getId();
    
    // 없으면 새로 생성
    ChatRoom newRoom = ChatRoom.builder()
        .isGroupChat("N")
        .name(member.getName() + "_" + otherMember.getName())
        .build();
    
    chatRoomRepository.save(newRoom);
    
    addParticipantToRoom(newRoom, member);
    addParticipantToRoom(newRoom, otherMember);
    
    return newRoom.getId();
  }
  
  
  // 채팅방 참여자 생성
  public void addParticipantToRoom(ChatRoom chatRoom, Member member) {
    ChatParticipant chatParticipant = ChatParticipant.builder()
        .chatRoom(chatRoom)
        .member(member)
        .build();
    
    chatParticipantRepository.save(chatParticipant);
  }
  
  public boolean isRoomParticipant(String email, long roomId) {
    
    ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("room cannot be found"));
    
    Member member = memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("room cannot be found"));
    
    List<ChatParticipant> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);
    for (ChatParticipant c : chatParticipants) {
      if (c.getMember().equals(member)) {
        return true;
      }
    }
    
    return false;
  }
  
  public void saveMessage(Long roomId, ChatMessageDTO chatMessageDTO) {
    
    // 채팅방 조회
    ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("room cannot found"));
    
    //보낸 사람 조회
    Member member = memberRepository.findByEmail(chatMessageDTO.getSenderEmail()).orElseThrow(() -> new EntityNotFoundException("room cannot found"));
    
    //메세지 저장
    ChatMessage chatMessage = ChatMessage.builder()
        .chatRoom(chatRoom)
        .member(member)
        .content(chatMessageDTO.getMessage())
        .build();
    
    chatMessageRepository.save(chatMessage);
    
    // 읽음 여부 저장
    List<ChatParticipant> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);
    for (ChatParticipant chatParticipant : chatParticipants) {
      ReadStatus readStatus = ReadStatus.builder()
          .chatRoom(chatRoom)
          .member(chatParticipant.getMember())
          .chatMessage(chatMessage)
          .isRead(chatParticipant.getMember().equals(member))
          .build();
      
      readStatusRepository.save(readStatus);
    }
  }
  
  //이전 메세지 조회
  public List<ChatMessageDTO> getHistory(Long roomId) {
    // 내가 해당 채팅방의 참여자가 아닐경우 에러
    ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("room cannot be found"));
    
    Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new EntityNotFoundException("room cannot be found"));
    
    List<ChatParticipant> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);
    
    boolean check = false;
    for (ChatParticipant chatParticipant : chatParticipants) {
      if (chatParticipant.getMember().equals(member)) {
        check = true;
      } // end if
    }// end for
    
    if (!check) throw new IllegalArgumentException("본인이 속하지 않은 채팅방입니다.");
    
    // 특정 room에 대한 message 조회
    List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomOrderByCreatedTimeAsc(chatRoom);
    List<ChatMessageDTO> chatMessageDTOS = new ArrayList<>();
    
    for (ChatMessage chatMessage : chatMessages) {
      ChatMessageDTO chatMessageDTO = ChatMessageDTO.builder()
          .message(chatMessage.getContent())
          .senderEmail(chatMessage.getMember().getEmail())
          .build();
      chatMessageDTOS.add(chatMessageDTO);
    }
    
    return chatMessageDTOS;
  }
}
