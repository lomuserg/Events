package ru.podol.events.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.podol.events.model.event.Participant;

@Repository
public interface ParticipantJpaRepository extends JpaRepository<Participant, Long> {

}
