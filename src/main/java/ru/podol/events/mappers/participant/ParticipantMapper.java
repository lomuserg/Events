package ru.podol.events.mappers.participant;

import org.mapstruct.*;
import ru.podol.events.dto.participant.ParticipantDto;
import ru.podol.events.model.participant.Participant;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ParticipantMapper {

    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "user.login", target = "login")
    @Mapping(source = "role", target = "role")
    ParticipantDto toParticipantDto(Participant participant);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "event", ignore = true)
    Participant toParticipant(ParticipantDto dto);

    List<ParticipantDto> toParticipantDtos(List<Participant> participants);

    List<Participant> toParticipants(List<ParticipantDto> dtos);
}
