package ru.podol.events.repozitory;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.podol.events.model.participant.Participant;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

}
