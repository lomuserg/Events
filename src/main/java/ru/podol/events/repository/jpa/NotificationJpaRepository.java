package ru.podol.events.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.podol.events.model.notification.Notification;

@Repository
public interface NotificationJpaRepository extends JpaRepository<Notification, Long> {
}
