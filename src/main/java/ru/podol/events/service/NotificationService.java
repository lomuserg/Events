package ru.podol.events.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.podol.events.dto.notification.NotificationDto;
import ru.podol.events.kafkaEvent.notifications.EventNotification;
import ru.podol.events.mappers.notification.KafkaEventNotificationMapper;
import ru.podol.events.mappers.notification.NotificationMapper;
import ru.podol.events.model.User;
import ru.podol.events.model.event.Event;
import ru.podol.events.model.notification.Notification;
import ru.podol.events.model.notification.NotificationType;
import ru.podol.events.model.participant.Participant;
import ru.podol.events.producer.notifications.KafkaEventNotificationsProducer;
import ru.podol.events.repository.NotificationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final KafkaEventNotificationsProducer kafkaProducer;
    private final NotificationMapper notificationMapper;
    private final KafkaEventNotificationMapper kafkaEventNotificationMapper;
    private final UserService userService;

    public void handleUserInvited(EventNotification userInvited){
        Notification notification = kafkaEventNotificationMapper.toNotification(userInvited);
        notification.setUser(userService.getUserById(userInvited.userId()));
        notificationRepository.save(notification);
    }

    public List<NotificationDto> getNotificationsByUserId(Long userId){
        List<Notification> notifications = notificationRepository.findByUserId(userId);
       return notificationMapper.toDtos(notifications);
    }

    public void sendReminder(Long userId, String title, NotificationType type, String message) {
        EventNotification notification = new EventNotification(userId, title, type, message);
        notificationRepository.save(kafkaEventNotificationMapper.toNotification(notification));
        kafkaProducer.sendMessage(notification);
    }

}
