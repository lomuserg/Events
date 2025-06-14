package ru.podol.events.kafkaEvent.notifications;

import ru.podol.events.model.notification.NotificationType;

public record UserInvited(Long userId, String eventTitle, NotificationType type, String eventMessage) {

}
