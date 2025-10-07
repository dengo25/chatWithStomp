package org.dengo.chat_backend.chat.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 스프링과 stomp는 기본적으로 세션관리를 자동으로(내부적)으로 관리
 * 연결해제 이벤트를 기록, 연결된 세션 수를 시시간으로 확인할 목적으로 이벤트 리스너를 생성 -> 로그,디버깅 목적
 */

@Component
@Log4j2
public class StompEventListener {
  
  // 멀티스레드환경에 안정적으로 사용될 수 있게 ConcurrentHashMap사용
  private final Set<String> sessions = ConcurrentHashMap.newKeySet();
  
  @EventListener //client가 웹소켓 연결이되면 자동으로 호출
  public void connectHandler(SessionConnectedEvent event) {
    
    //이벤트에 담긴 STOMP 메시지의 헤더 정보를 StompHeaderAccessor를 이용해 읽을 수 있는 상태로 변환한다는
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
    sessions.add(accessor.getSessionId()); //연결된 세션정보를 저장
    log.info("connect session ID" + accessor.getSessionId());
    log.info("total session : " + sessions.size());
  }
  
  @EventListener
  public void disconnectHandler(SessionDisconnectEvent event) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
    sessions.remove(accessor.getSessionId());
    log.info("disconnect session ID" + accessor.getSessionId());
    log.info("total session : " + sessions.size());
    
  }
}
