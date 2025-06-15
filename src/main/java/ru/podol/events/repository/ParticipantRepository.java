package ru.podol.events.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.podol.events.model.participant.Participant;
import ru.podol.events.repository.jpa.ParticipantJpaRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ParticipantRepository {
    private final ParticipantJpaRepository participantJpaRepository;

    public Participant save(Participant participant) {
        return participantJpaRepository.save(participant);
    }

    public List<Participant> saveAll(List<Participant> participants) {
        return participantJpaRepository.saveAll(participants);
    }

    public Participant update(Participant participant) {
        return participantJpaRepository.save(participant);
    }

    public void delete(Participant participant) {
        participantJpaRepository.delete(participant);
    }

    public Participant findById(Long id) {
        return participantJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Participant not found"));
    }

    public Participant findByEventIdAndUserLogin(Long eventId, String login) {
        return participantJpaRepository.findByEventIdAndUserLogin(eventId, login)
                .orElseThrow(() -> new RuntimeException("Participant not found"));
    }

    public Participant findByEventIdAndUserId(Long eventId, Long userId) {
        return participantJpaRepository.findByEventIdAndUserId(eventId, userId)
                .orElseThrow(() -> new RuntimeException("Participant not found"));
    }

    public List<Participant> findParticipantsForReminder(LocalDateTime start, LocalDateTime end) {
        return participantJpaRepository.findParticipantsForReminder(start, end);
    }

}
