package ru.podol.events.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.podol.events.model.User;
import ru.podol.events.model.organizer.Organizer;

import java.util.Optional;

public interface OrganizerJpaRepository extends JpaRepository<Organizer, Long> {
    Optional<Organizer> findByUser(User user);
}
