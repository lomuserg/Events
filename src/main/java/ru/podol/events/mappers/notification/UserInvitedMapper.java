package ru.podol.events.mappers.notification;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.podol.events.kafkaEvent.notifications.UserInvited;
import ru.podol.events.model.notification.Notification;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserInvitedMapper {
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "title", source = "eventTitle")
    @Mapping(target = "message", source = "eventMessage")
    @Mapping(target = "read", constant = "false")
    @Mapping(target = "type", source = "type")
    Notification toNotification(UserInvited userInvited);
}
