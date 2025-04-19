package ru.podol.events.repozitory;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.podol.events.model.organizer.Organizer;

public interface OrganizerRepository extends JpaRepository<Organizer, Long> {

}
