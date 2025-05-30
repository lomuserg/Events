package ru.podol.events.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.podol.events.repository.jpa.NotificationJpaRepository;

import javax.management.Notification;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class NotificationRepository {
    private final NotificationJpaRepository notificationJpaRepository;

//    public Notification save(Notification notification) {
//        return notificationJpaRepository.save(notification);
//    }
//
//    public Notification update(Notification notification) {
//        return notificationJpaRepository.save(notification);
//    }
//
//    public void delete(Notification notification) {
//        notificationJpaRepository.delete(notification);
//    }
//
//    public Notification findById(Long id) {
//        return notificationJpaRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Event not found"));
//    }

//    public List<Notification> findByUserId(Long userId) {
//        return notificationJpaRepository.findByUserId(userId);
//    }
}
