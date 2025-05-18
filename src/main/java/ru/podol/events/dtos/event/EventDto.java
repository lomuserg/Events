package ru.podol.events.dtos.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.podol.events.model.UserEventRole;
import ru.podol.events.model.event.EventCategory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private Long id;
    private String title;
    private String description;
    private String location;
    private EventCategory eventCategory;
    private UserEventRole userEventRole;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime eventDateTime;

    @ToString.Exclude
    private List<String> participantsLogins = new ArrayList<>();
}
