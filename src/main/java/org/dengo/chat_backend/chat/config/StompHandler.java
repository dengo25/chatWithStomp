package org.dengo.chat_backend.chat.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.dengo.chat_backend.chat.service.ChatService;
import org.dengo.chat_backend.util.JWTTokenProvider;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

import static org.dengo.chat_backend.util.JWTFilter.secretKey;

@Component
@RequiredArgsConstructor
// ChannelInterceptor를 구현함으로써 STOMP 연결 과정에서 메시지를 가로채고 검증
public class StompHandler implements ChannelInterceptor {
  
  private final ChatService chatService;
  private final JWTTokenProvider jwtTokenProvider;
  
  @Override //WebSocket 메시지가 브로커로 보내지기 직전에 호출
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    
    // STOMP 헤더를 읽기 쉽게 StompHeaderAccessor로 감싼다 헤더값을 추출하기 위함
    final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
    
    // 클라이언트가 WebSocket 연결을 처음 맺을 때(CONNECT 요청일 때)
    if (StompCommand.CONNECT == accessor.getCommand()) {
      System.out.println("connect요청시 토큰 유효성 검증");
      
      // STOMP 헤더 중 Authorization 값을 가져 옴
      String bearerToken = accessor.getFirstNativeHeader("Authorization");
      String token = bearerToken.substring(7);
      
      // 토큰 검증
      Claims claims = jwtTokenProvider.parseToken(token);
      
      System.out.println("토큰 검증 완료");
    } //end if
    
    // 구독 요청시 권한 확인 로직
    if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
      String bearerToken = accessor.getFirstNativeHeader("Authorization");
      String token = bearerToken.substring(7);
      
      //토큰 검증
      Claims claims = jwtTokenProvider.parseToken(token);
      
      String email = claims.getSubject();
      
      // 구독하려는 채팅방의 ID를 STOMP destination에서 추출
      String roomId = accessor.getDestination().split("/")[2];
      if (!chatService.isRoomParticipant(email, Long.parseLong(roomId))) {
        throw new AuthenticationServiceException("해당 room에 권한이 없습니다.");
      }
    }
    
    return message;
  }
}
