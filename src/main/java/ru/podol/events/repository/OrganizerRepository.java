package ru.podol.events.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.podol.events.model.User;
import ru.podol.events.model.organizer.Organizer;
import ru.podol.events.repository.jpa.OrganizerJpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrganizerRepository {

    private final OrganizerJpaRepository organizerJpaRepository;

    public Organizer save(Organizer organizer) {
        return organizerJpaRepository.save(organizer);
    }

    public List<Organizer> findAll() {
        return organizerJpaRepository.findAll();
    }

    public Organizer findById(Long id) {
        return organizerJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organizer not found"));
    }

    public Optional<Organizer> findByUser(User user) {
        return organizerJpaRepository.findByUser(user);
    }
}
