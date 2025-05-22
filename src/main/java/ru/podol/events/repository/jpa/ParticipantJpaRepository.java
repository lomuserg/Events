package ru.podol.events.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.podol.events.model.participant.Participant;

import java.util.Optional;

@Repository
public interface ParticipantJpaRepository extends JpaRepository<Participant, Long> {
    Optional<Participant> findByEventIdAndUserLogin(Long eventId, String login);
    Optional<Participant> findByEventIdAndUserId(Long eventId, Long userId);
}
