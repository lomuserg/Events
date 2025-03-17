package ru.podol.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.podol.events.repozitory.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
@RequiredArgsConstructor
public class UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;


}
