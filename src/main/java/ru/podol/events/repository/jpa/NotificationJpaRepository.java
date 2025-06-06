package ru.podol.events.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.podol.events.model.notification.Notification;
import ru.podol.events.model.participant.Participant;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationJpaRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long userId);
}
