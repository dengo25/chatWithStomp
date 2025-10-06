package org.dengo.chat_backend.chat.service;

import lombok.RequiredArgsConstructor;
import org.dengo.chat_backend.chat.repository.ChatMessageRepository;
import org.dengo.chat_backend.chat.repository.ChatParticipantRepository;
import org.dengo.chat_backend.chat.repository.ChatRoomRepository;
import org.dengo.chat_backend.chat.repository.ReadStatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {
  
  private final ChatRoomRepository chatRoomRepository;
  private final ChatParticipantRepository chatParticipantRepository;
  private final ChatMessageRepository chatMessageRepository;
  private final ReadStatusRepository readStatusRepository;
}
