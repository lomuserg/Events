package ru.podol.events.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.podol.events.model.event.Participant;
import ru.podol.events.repository.jpa.ParticipantJpaRepository;

@Repository
@RequiredArgsConstructor
public class ParticipantRepository {
    private ParticipantJpaRepository participantJpaRepository;

    public Participant save(Participant participant) {
        return participantJpaRepository.save(participant);
    }

    public Participant update(Participant participant) {
        return participantJpaRepository.save(participant);
    }

    public void delete(Participant participant) {
        participantJpaRepository.delete(participant);
    }

}
