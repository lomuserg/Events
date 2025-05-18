package ru.podol.events.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.podol.events.dtos.event.EventDto;
import ru.podol.events.mappers.event.EventMapper;
import ru.podol.events.model.event.Event;
import ru.podol.events.model.User;
import ru.podol.events.model.event.EventUser;
import ru.podol.events.model.UserEventRole;
import ru.podol.events.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final UserService userService;

    public EventDto createEvent(Long userId, EventDto eventDto) {
        User user = userService.getUserById(userId);
        log.info("Creating new event by {}", user.getLogin().toString());

        Event event = eventMapper.toEvent(eventDto);
        Event savedEvent = eventRepository.save(event);

        EventUser eventUser = new EventUser();
        eventUser.setUser(user);
        eventUser.setEvent(savedEvent);
        eventUser.setRole(UserEventRole.ORGANIZER);
        savedEvent.getParticipants().add(eventUser);

        log.info("Creating new event {}", savedEvent.getTitle().toString());
        return eventMapper.toEventDto(eventRepository.save(savedEvent));
    }

    public List<EventDto> getEventsByUserId(Long userId) {
        List<Event> events = eventRepository.findByUserId(userId);
        return eventMapper.toEventDtos(events);
    }

    public EventDto getEventById(Long userId) {
        Event event = eventRepository.findById(userId);
        return eventMapper.toEventDto(event);
    }

    public List<EventDto> getEventsByUserIdWithRoles(Long userId) {
        List<Object[]> results = eventRepository.findEventsWithRolesByUserId(userId);

        return results.stream()
                .map(result -> {
                    Event event = (Event) result[0];
                    UserEventRole role = (UserEventRole) result[1];

                    EventDto dto = eventMapper.toEventDto(event);
                    dto.setUserEventRole(role);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    //public EventDto addParticipantToEvent(Long eventId, Long userId) {}

}