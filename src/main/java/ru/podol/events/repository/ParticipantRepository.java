package ru.podol.events.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.podol.events.model.participant.Participant;
import ru.podol.events.repository.jpa.ParticipantJpaRepository;

@Repository
@RequiredArgsConstructor
public class ParticipantRepository {
    private final ParticipantJpaRepository participantJpaRepository;

    public Participant save(Participant participant) {
        return participantJpaRepository.save(participant);
    }

    public Participant update(Participant participant) {
        return participantJpaRepository.save(participant);
    }

    public void delete(Participant participant) {
        participantJpaRepository.delete(participant);
    }

    public Participant findByEventIdAndUserLogin(Long eventId, String login) {
        return participantJpaRepository.findByEventIdAndUserLogin(eventId, login)
                .orElseThrow(() -> new RuntimeException("Participant not found"));
    }

}
