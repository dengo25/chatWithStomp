package org.dengo.chat_backend.chat.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker //STOMP over WebSocket(메시지 브로커 사용)을 활성화, 내부적으로 메시지 브로커와 STOMP 핸들링을 설정
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
  
  private final StompHandler stompHandler;
  
  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/connect")
        .setAllowedOrigins("http://localhost:5173", "https://chat.thekosta.com")
        // ws:// 가 아니라 http:// 엔드포인트를 사용할 수 있게 해주는 sockJs 라이브러를 통한 요청을 허용하는 설정
        .withSockJS();
  }
  
  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    //   /publish/{roomNumber} 형태로 메세지를 발행
    //   /publish 형태로 시작하는 url패턴으로 메세지가 발행되면 @Controller 객체의 @messageMapping 메서드로 라우팅
    registry.setApplicationDestinationPrefixes("/publish");
    
    //   /topic/{roomNumber} 형태로 메세지를 수신해야 함을 설정
    registry.enableSimpleBroker("/topic");
  }
  
  // 클라이언트 → 서버로 들어오는 모든 STOMP 프레임(CONNECT, SEND, SUBSCRIBE 등)을 처리하는 채널을 설정
  // 웹소켓 요청{connect, subscribe, disconnect} 등의 요청시에서는 http header등 http메세지를 넣어올 수 있고,
  //  이를 Interceptor를 통해 가로채 토큰등을 검증할 수 있음
  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(stompHandler);
  }
}
