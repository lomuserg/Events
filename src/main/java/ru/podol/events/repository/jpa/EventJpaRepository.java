package ru.podol.events.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.podol.events.model.event.Event;
import ru.podol.events.model.event.EventUser;

import java.util.List;

@Repository
public interface EventJpaRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e JOIN e.participants eu WHERE eu.user.id = :userId")
    List<Event> findByUserId(Long userId);

    @Query("SELECT eu.event, eu.role FROM EventUser eu WHERE eu.user.id = :userId")
    List<Object[]> findEventsWithRolesByUserId(Long userId);
}
