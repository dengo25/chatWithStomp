package org.dengo.chat_backend.chat.controller;

import lombok.RequiredArgsConstructor;
import org.dengo.chat_backend.chat.dto.ChatMessageDTO;
import org.dengo.chat_backend.chat.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
  
  private final ChatService chatService;
  
  @PostMapping("/room/private/create")
  public ResponseEntity<?> getOrCreatePrivateRoom(@RequestParam Long otherMemberId) {
    Long roomId = chatService.getOrCreatePrivateRoom(otherMemberId);
    return new ResponseEntity<>(roomId, HttpStatus.OK);
  }
  
  //이전 메세지 조회
  @GetMapping("/history/{roomId}")
  public ResponseEntity<?> getHistory(@PathVariable Long roomId) {
    List<ChatMessageDTO> chatMessageDTOS = chatService.getHistory(roomId);
    return new ResponseEntity<>(chatMessageDTOS, HttpStatus.OK);
  }
}
