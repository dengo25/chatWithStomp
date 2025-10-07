package org.dengo.chat_backend.member.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.dengo.chat_backend.member.domain.Member;
import org.dengo.chat_backend.member.dto.MemberLoginReqDTO;
import org.dengo.chat_backend.member.dto.MemberSaveReqDTO;
import org.dengo.chat_backend.member.repository.MemberRepository;
import org.dengo.chat_backend.util.JWTTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
  
  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final JWTTokenProvider jwtTokenProvider;
  
  public Member create(MemberSaveReqDTO memberSaveReqDTO) {
    
    if (memberRepository.findByEmail(memberSaveReqDTO.getEmail()).isPresent()) {
      throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
    }
    
    Member member = Member.builder()
        .email(memberSaveReqDTO.getEmail())
        .password(passwordEncoder.encode(memberSaveReqDTO.getPassword()))
        .build();
    
    Member newMember = memberRepository.save(member);
    return newMember;
  }
  
  
  public Member login(MemberLoginReqDTO memberLoginReqDTO) {
    Member member = memberRepository.findByEmail(memberLoginReqDTO.getEmail())
        .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
    
    if (!passwordEncoder.matches(memberLoginReqDTO.getPassword(), member.getPassword())) {
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }
    
    return member;
  }
  
  public  Map<String, Object> getInfo(Member member) {
    String jwtToken = jwtTokenProvider.createToken(member.getEmail(), member.getRole().toString());
    Map<String, Object> loginInfo = new HashMap<>();
    loginInfo.put("id", member.getId());
    loginInfo.put("token", jwtToken);
    return loginInfo;
  }
}
