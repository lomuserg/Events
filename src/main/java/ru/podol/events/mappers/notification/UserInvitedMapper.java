package ru.podol.events.mappers.notification;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.podol.events.events.notifications.UserInvited;
import ru.podol.events.model.notification.Notification;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserInvitedMapper {
    @Mapping(target = "user", source = "userInvited")
    @Mapping(target = "title", expression = "java(userInvited.eventTitle())")
    @Mapping(target = "message", expression = "java(userInvited.eventTitle())")
    @Mapping(target = "read", constant = "false")
    Notification toNotification(UserInvited userInvited);
}
