package ru.podol.events.dtos.event;

import lombok.Data;
import ru.podol.events.model.event.EventCategory;

import java.time.LocalDateTime;

@Data
public class EventDto {
    private String title;
    private String description;
    private LocalDateTime eventDateTime;
    private String location;
    private EventCategory eventCategory;
}
