package ru.podol.events.dtos.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.podol.events.model.event.EventCategory;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private String title;
    private String description;
    private LocalDateTime eventDateTime;
    private String location;
    private EventCategory eventCategory;
}
