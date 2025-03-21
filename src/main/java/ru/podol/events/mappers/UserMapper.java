package ru.podol.events.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.podol.events.dtos.SignUpDto;
import ru.podol.events.dtos.UserDto;
import ru.podol.events.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    @Mapping(target = "password", ignore = true)
    User signUpToUser(SignUpDto signUpDto);

}
