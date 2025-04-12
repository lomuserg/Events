package ru.podol.events.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "event_datetime", nullable = false)
    private LocalDateTime eventDateTime;

    @Column(length = 255)
    private String location;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Организатор (отдельная сущность)
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "organizer_id", nullable = false)
//    private Organizer organizer;

    // Участники мероприятия
//    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Participant> participants = new ArrayList<>();

    // Категория мероприятия
//    @ManyToOne
//    @JoinColumn(name = "category_id")
//    private Category category;

}

