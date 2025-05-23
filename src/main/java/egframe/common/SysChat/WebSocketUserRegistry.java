package egframe.common.SysChat;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//@Component
public class WebSocketUserRegistry {

    private final Map<String, Integer> topicUserCounts = new ConcurrentHashMap<>();
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketUserRegistry(@Lazy SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent event) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String destination = headerAccessor.getDestination(); // 구독한 토픽

        topicUserCounts.merge(destination, 1, Integer::sum); // 사용자 수 증가
        broadcastUserCount(destination);
    }

    @EventListener
    public void handleUnsubscribeEvent(SessionUnsubscribeEvent event) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String destination = headerAccessor.getDestination(); // 구독 해제된 토픽

        topicUserCounts.merge(destination, -1, Integer::sum); // 사용자 수 감소
        if (topicUserCounts.get(destination) <= 0) {
            topicUserCounts.remove(destination); // 사용자 0명일 경우 제거
        } else {
            broadcastUserCount(destination);
        }
    }

    private void broadcastUserCount(String topic) {
        int count = topicUserCounts.getOrDefault(topic, 0);
        messagingTemplate.convertAndSend(topic + "/count", count); // 사용자 수 전송
    }
}