package ru.podol.events.controllers.notifications;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.podol.events.dtos.UserDto;
import ru.podol.events.dtos.notification.NotificationDto;
import ru.podol.events.services.NotificationService;

import java.util.Collections;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/main/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getNotificationsByUserId(@AuthenticationPrincipal UserDto userDto) {
        if (isUserDtoIsNull(userDto)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList());
        }
        List<NotificationDto> notifications = notificationService.getNotificationsByUserId(userDto.getId());

        return ResponseEntity.ok(notifications);
    }

    private boolean isUserDtoIsNull(UserDto userDto) {
        return userDto == null;
    }

}
