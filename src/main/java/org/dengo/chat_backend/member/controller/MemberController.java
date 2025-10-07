package org.dengo.chat_backend.member.controller;

import lombok.RequiredArgsConstructor;
import org.dengo.chat_backend.member.domain.Member;
import org.dengo.chat_backend.member.dto.MemberListResDTO;
import org.dengo.chat_backend.member.dto.MemberLoginReqDTO;
import org.dengo.chat_backend.member.dto.MemberSaveReqDTO;
import org.dengo.chat_backend.member.service.MemberService;
import org.dengo.chat_backend.util.JWTTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
  
  private final MemberService memberService;
  private final JWTTokenProvider jwtTokenProvider;
  
  @PostMapping("/create")
  public ResponseEntity<?> memberCreate(@RequestBody MemberSaveReqDTO memberSaveReqDTO) {
    Member member = memberService.create(memberSaveReqDTO);
    return new ResponseEntity<>(member.getId(), HttpStatus.CREATED);
  }
  
  @PostMapping("/doLogin")
  public ResponseEntity<?> doLogin(@RequestBody MemberLoginReqDTO memberLoginReqDTO) {
    
    Member member = memberService.login(memberLoginReqDTO);
    
    //토큰 발행 로직
    Map<String, Object> loginInfo = memberService.getInfo(member);
    
    return new ResponseEntity<>(loginInfo, HttpStatus.OK);
  }
  
  @GetMapping("/list")
  public ResponseEntity<?> memberList() {
    List<MemberListResDTO> dtos = memberService.findAll();
    return new ResponseEntity<>(dtos, HttpStatus.OK);
  }

}
