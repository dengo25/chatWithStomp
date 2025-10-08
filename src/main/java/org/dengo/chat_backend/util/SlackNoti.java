package org.dengo.chat_backend.util;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class SlackNoti {
  
  @Value("${slack.webhook.url}")
  private String webhookUrl;
  
  public void sendChatNotification(Long memberId, String senderName, String message, Long roomId) {
    if (memberId != 1L) {
      return;
    }
    
    try {
      Slack slack = Slack.getInstance();
      
      String text = String.format("새 메시지가 도착했습니다!\n발신자: %s\n내용: %s\n채팅방 ID: %d",
          senderName, message, roomId);
      
      Payload payload = Payload.builder()
          .text(text)
          .build();
      
      slack.send(webhookUrl, payload);
      
      log.info("알람 "+ memberId);
    } catch (Exception e) {
      log.error("fail send slack", e);
    }
  }
}
