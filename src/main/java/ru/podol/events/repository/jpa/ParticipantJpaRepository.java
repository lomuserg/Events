package ru.podol.events.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.podol.events.model.participant.Participant;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantJpaRepository extends JpaRepository<Participant, Long> {

    @Query("SELECT p FROM Participant p " +
            "JOIN FETCH p.user " +
            "JOIN FETCH p.event " +
            "WHERE p.event.eventDateTime BETWEEN :start AND :end " +
            "AND p.reminderSent = false")
    List<Participant> findParticipantsForReminder(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    Optional<Participant> findByEventIdAndUserLogin(Long eventId, String login);
    Optional<Participant> findByEventIdAndUserId(Long eventId, Long userId);
}
