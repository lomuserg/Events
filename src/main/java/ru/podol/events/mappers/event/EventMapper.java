package ru.podol.events.mappers.event;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.podol.events.dtos.event.EventDto;
import ru.podol.events.model.event.Event;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(target = "participantsLogins", ignore = true)
    EventDto toEventDto(Event event);

    @Mapping(source = "id", target = "id")
    @Mapping(target = "participants", ignore = true)
    Event toEvent(EventDto eventDto);

    List<EventDto> toEventDtos(List<Event> events);

    List<Event> toEvents(List<EventDto> eventDtos);
}
