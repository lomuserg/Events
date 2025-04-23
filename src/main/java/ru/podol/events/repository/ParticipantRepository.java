package ru.podol.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.podol.events.model.participant.Participant;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

}
