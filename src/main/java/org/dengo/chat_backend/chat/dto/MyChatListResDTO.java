package org.dengo.chat_backend.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyChatListResDTO {
  
  private Long roomId;
  private String roomName;
  private String isGroupChat;
  private Long unReadCount;
}
