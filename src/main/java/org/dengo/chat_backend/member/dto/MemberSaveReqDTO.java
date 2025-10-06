package org.dengo.chat_backend.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberSaveReqDTO {
  
  private Long id;
  private String email;
  private String password;
  
}
