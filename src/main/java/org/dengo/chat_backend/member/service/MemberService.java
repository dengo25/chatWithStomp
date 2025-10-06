package org.dengo.chat_backend.member.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.dengo.chat_backend.member.domain.Member;
import org.dengo.chat_backend.member.dto.MemberLoginReqDTO;
import org.dengo.chat_backend.member.dto.MemberSaveReqDTO;
import org.dengo.chat_backend.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
  
  private final MemberRepository memberRepository;
  
  public Member create(MemberSaveReqDTO memberSaveReqDTO) {
    
    if (memberRepository.findByEmail(memberSaveReqDTO.getEmail()).isPresent()) {
      throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
    }
    
    Member member = Member.builder()
        .email(memberSaveReqDTO.getEmail())
        .password(memberSaveReqDTO.getPassword())
        .build();
    
    Member newMember = memberRepository.save(member);
    return newMember;
  }
  
  
  public Member login(MemberLoginReqDTO memberLoginReqDTO) {
    Member member = memberRepository.findByEmail(memberLoginReqDTO.getEmail())
        .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
    
    return member;
  }
}
