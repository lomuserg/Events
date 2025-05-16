package ru.podol.events.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.podol.events.model.event.Event;


import java.util.List;

@Repository
public interface EventJpaRepository extends JpaRepository<Event, Long> {

}
