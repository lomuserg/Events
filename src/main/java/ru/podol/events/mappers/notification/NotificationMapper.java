package ru.podol.events.mappers.notification;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.podol.events.dtos.notification.NotificationDto;
import ru.podol.events.model.notification.Notification;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "read", target = "read")
    @Mapping(source = "createdAt", target = "createdAt")
    NotificationDto toDto(Notification notification);

    Notification toEntity(NotificationDto notificationDto);

    List<NotificationDto> toDtos(List<Notification> notifications);
}
