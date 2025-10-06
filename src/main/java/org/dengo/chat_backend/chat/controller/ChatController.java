package org.dengo.chat_backend.chat.controller;

import lombok.RequiredArgsConstructor;
import org.dengo.chat_backend.chat.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
