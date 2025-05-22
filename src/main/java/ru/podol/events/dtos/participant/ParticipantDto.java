package ru.podol.events.dtos.participant;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import ru.podol.events.model.UserEventRole;

import java.time.LocalDateTime;

@Data
public class ParticipantDto {
    private Long eventId;
    private Long userId;
    private String login;
    private UserEventRole role;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
