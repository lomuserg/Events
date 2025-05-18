package ru.podol.events.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.podol.events.model.User;
import ru.podol.events.model.UserEventRole;
import ru.podol.events.model.event.Event;
import ru.podol.events.model.event.EventUser;
import ru.podol.events.repository.jpa.EventJpaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class EventRepository {
    private final EventJpaRepository eventJpaRepository;

    public Event save(Event event) {
        return eventJpaRepository.save(event);
    }

    public List<Event> findAll() {
        return eventJpaRepository.findAll();
    }

    public Event findById(Long id) {
        return eventJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    public List<Event> findByUserId(Long userId) {
        return eventJpaRepository.findByUserId(userId);
    }

    public List<Object[]> findEventsWithRolesByUserId(Long userId) {
        return eventJpaRepository.findEventsWithRolesByUserId(userId);
    }



}
