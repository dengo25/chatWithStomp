package org.dengo.chat_backend.member.controller;

import lombok.RequiredArgsConstructor;
import org.dengo.chat_backend.member.domain.Member;
import org.dengo.chat_backend.member.dto.MemberSaveReqDTO;
import org.dengo.chat_backend.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
  
  private final MemberService memberService;
  
  @PostMapping("/create")
  public ResponseEntity<?> memberCreate(MemberSaveReqDTO memberSaveReqDTO) {
    Member member = memberService.create(memberSaveReqDTO);
    return new ResponseEntity<>(member.getId(), HttpStatus.CREATED);
  }
}
