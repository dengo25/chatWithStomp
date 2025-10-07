package org.dengo.chat_backend.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.dengo.chat_backend.chat.dto.ChatMessageDTO;
import org.dengo.chat_backend.chat.service.ChatService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Log4j2
public class StompController {
  
  private final SimpMessageSendingOperations messageSendingOperations;
  private final ChatService chatService;
  
  @MessageMapping("/{roomId}") //클라이언트에서 특성 publish/roomId 형태로 메세지를 발행시 MessageMapping 수신
  public void sendMessage(@DestinationVariable Long roomId, ChatMessageDTO chatMessageDTO) {
    
    log.info(chatMessageDTO.getMessage());
    
    chatService.saveMessage(roomId, chatMessageDTO);
    messageSendingOperations.convertAndSend("/topic/" + roomId, chatMessageDTO);
  }
}
