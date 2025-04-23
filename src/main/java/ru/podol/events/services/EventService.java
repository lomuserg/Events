package ru.podol.events.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.podol.events.dtos.event.EventDto;
import ru.podol.events.model.User;
import ru.podol.events.model.event.Event;
import ru.podol.events.model.organizer.Organizer;
import ru.podol.events.repository.OrganizerRepository;
import ru.podol.events.repository.jpa.EventJpaRepository;
import ru.podol.events.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventJpaRepository eventRepository;
    private final OrganizerRepository organizerRepository;
    private final UserRepository userRepository;

    public Event createEvent(Long userId, EventDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Organizer organizer = organizerRepository.findByUser(user)
                .orElseGet(() -> {
                    Organizer newOrganizer = new Organizer();
                    newOrganizer.setUser(user);
                    newOrganizer.setOrganizerLogin(user.getLogin());
                    return organizerRepository.save(newOrganizer);
                });
        Event event = new Event();
        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setEventDateTime(dto.getEventDateTime());
        event.setLocation(dto.getLocation());
        event.setEventCategory(dto.getEventCategory());
       event.setOrganizer(organizer);

        return eventRepository.save(event);
    }
}
