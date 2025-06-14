package ru.podol.events.mappers.notification;

import org.mapstruct.*;
import ru.podol.events.kafkaEvent.notifications.EventNotification;
import ru.podol.events.model.notification.Notification;


@Mapper(componentModel = "spring")
public interface KafkaEventNotificationMapper {

    Notification toNotification(EventNotification eventNotification);
}
