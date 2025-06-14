package ru.podol.events.mappers.notification;

import org.mapstruct.*;
import ru.podol.events.kafkaEvent.notifications.EventNotification;
import ru.podol.events.model.notification.Notification;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface KafkaEventNotificationMapper {
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "title", source = "eventTitle")
    @Mapping(target = "message", source = "eventMessage")
    @Mapping(target = "read", constant = "false")
    @Mapping(target = "type", source = "type")
    Notification toNotification(EventNotification eventNotification);
}
