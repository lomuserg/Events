package ru.podol.events.listener.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.podol.events.events.notifications.UserInvited;
import ru.podol.events.services.NotificationService;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserInvitedListener {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${spring.kafka.topic.user-invited.name}", groupId = "notification-group")
    public void listen(String message) {
        try {
            UserInvited userInvited = objectMapper.readValue(message, UserInvited.class);
            //notificationService.handleUserInvited(userInvited);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse UserInvited event", e);
        }
    }
}

