package ru.podol.events.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.podol.events.dtos.UserDto;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal UserDto userDto) {
        if (userDto == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }
        return ResponseEntity.ok(userDto);
    }

    @PutMapping
    public ResponseEntity<?> updateUserInfo(@AuthenticationPrincipal UserDto userDto, @RequestBody UserDto updatedUserDto) {
        if (userDto == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }
        userDto.setFirstName(updatedUserDto.getFirstName());
        userDto.setLastName(updatedUserDto.getLastName());

        return ResponseEntity.ok(userDto);
    }


}
