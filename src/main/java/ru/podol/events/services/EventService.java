package ru.podol.events.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.podol.events.dtos.event.EventDto;
import ru.podol.events.dtos.participant.ParticipantDto;
import ru.podol.events.mappers.event.EventMapper;
import ru.podol.events.model.event.Event;
import ru.podol.events.model.User;
import ru.podol.events.model.participant.Participant;
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
    private final ParticipantsService participantsService;

    public Event findById(Long id) {
        return eventRepository.findById(id);
    }

    public EventDto createEvent(Long userId, EventDto eventDto) {
        User user = userService.getUserById(userId);
        log.info("Creating new event by {}", user.getLogin().toString());

        Event event = eventMapper.toEvent(eventDto);
        Event savedEvent = eventRepository.save(event);

        Participant participant = new Participant();
        participant.setUser(user);
        participant.setEvent(savedEvent);
        participant.setRole(UserEventRole.ORGANIZER);
        savedEvent.getParticipants().add(participant);

        log.info("Creating new event {}", savedEvent.getTitle().toString());
        return eventMapper.toEventDto(eventRepository.save(savedEvent));
    }

    @Transactional
    public EventDto updateEvent(EventDto eventDto, long eventId) {
        Event existingEvent = eventRepository.findById(eventId);
        log.info("Updating event: {} (id={})", existingEvent.getTitle(), eventId);
        eventMapper.updateEventFromDto(eventDto, existingEvent);

        Event updatedEvent = eventRepository.update(existingEvent);
        log.info("Event {} updated successfully", updatedEvent.getId());
        return eventMapper.toEventDto(updatedEvent);
    }

    @Transactional
    public void deleteEvent(Long eventId) {
        Event event = eventRepository.findById(eventId);
        log.info("Delete event: {} (id={})", event.getTitle(), eventId);
        eventRepository.delete(event);
    }

    public EventDto getEventById(Long userId) {
        Event event = eventRepository.findById(userId);
        return eventMapper.toEventDto(event);
    }

    public EventDto getEventByIdAndUserId(Long userId, Long eventId) {
        Event event = eventRepository.findById(userId);
        Participant participant = participantsService.findParticipantByUserIdAndEventId(eventId, userId);
        event.setParticipants(List.of(participant));
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

    public List<EventDto> getEventsByUserId(Long userId) {
        List<Event> events = eventRepository.findByUserId(userId);
        return eventMapper.toEventDtos(events);
    }

}