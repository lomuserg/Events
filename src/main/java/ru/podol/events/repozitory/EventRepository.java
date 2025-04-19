package ru.podol.events.repozitory;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.podol.events.model.event.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

}
