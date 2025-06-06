package ru.podol.events.services;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.podol.events.events.notifications.UserInvited;
import ru.podol.events.mappers.notification.UserInvitedMapper;
import ru.podol.events.model.notification.Notification;
import ru.podol.events.repository.NotificationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserInvitedMapper userInvitedMapper;
    @Lazy
    private final UserService userService;

    public void handleUserInvited(UserInvited userInvited){
        Notification notification = userInvitedMapper.toNotification(userInvited);
        notification.setUser(userService.getUserById(userInvited.userId()));
        notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsByUserId(Long userId){
       return notificationRepository.findByUserId(userId);
    }


}
