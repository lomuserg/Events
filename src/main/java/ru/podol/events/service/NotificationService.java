package ru.podol.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.podol.events.dto.notification.NotificationDto;
import ru.podol.events.kafkaEvent.notifications.UserInvited;
import ru.podol.events.mappers.notification.NotificationMapper;
import ru.podol.events.mappers.notification.UserInvitedMapper;
import ru.podol.events.model.notification.Notification;
import ru.podol.events.repository.NotificationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserInvitedMapper userInvitedMapper;
    private final NotificationMapper notificationMapper;

    @Lazy
    private final UserService userService;

    public void handleUserInvited(UserInvited userInvited){
        Notification notification = userInvitedMapper.toNotification(userInvited);
        notification.setUser(userService.getUserById(userInvited.userId()));
        notificationRepository.save(notification);
    }

    public List<NotificationDto> getNotificationsByUserId(Long userId){
        List<Notification> notifications = notificationRepository.findByUserId(userId);
       return notificationMapper.toDtos(notifications);
    }


}
