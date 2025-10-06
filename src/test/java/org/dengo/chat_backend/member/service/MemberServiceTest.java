package org.dengo.chat_backend.member.service;

import org.dengo.chat_backend.member.domain.Member;
import org.dengo.chat_backend.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {
  
  @Autowired
  private MemberRepository memberRepository;
  
  @Test
  void EmailCheck() {
    Member member = Member.builder()
        .email("test@test.com")
        .password("1234")
        .build();
    Member newMember = memberRepository.save(member);
    assertThat(newMember.getEmail()).isEqualTo("test@test.com");
  }
  
  
}