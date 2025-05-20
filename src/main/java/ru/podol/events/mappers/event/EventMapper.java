package ru.podol.events.mappers.event;

import org.mapstruct.*;
import ru.podol.events.dtos.event.EventDto;
import ru.podol.events.model.event.Event;
import ru.podol.events.model.event.Participant;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    @Mapping(source = "event.participants", target = "participantsLogins", qualifiedByName = "mapEventUsersToLogins")
    EventDto toEventDto(Event event);

    @Mapping(source = "id", target = "id")
    @Mapping(target = "participants", ignore = true)
    Event toEvent(EventDto eventDto);

    List<EventDto> toEventDtos(List<Event> events);

    List<Event> toEvents(List<EventDto> eventDtos);

    @Mapping(target = "id", ignore = true)
    void updateEventFromDto(EventDto dto, @MappingTarget Event event);

    @Named("mapEventUsersToLogins")
    default List<String> mapEventUsersToLogins(List<Participant> participants) {
        if (participants == null) {
            return List.of();
        }

        return participants.stream()
                .map(eu -> eu.getUser().getLogin())
                .toList();
    }
}
