package ru.podol.events.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.podol.events.dtos.UserDto;
import ru.podol.events.dtos.auth.CredentialsDto;
import ru.podol.events.dtos.auth.SignUpDto;
import ru.podol.events.exceptions.AppException;
import ru.podol.events.mappers.UserMapper;
import ru.podol.events.model.User;
import ru.podol.events.repository.UserRepository;

import java.nio.CharBuffer;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    private final String rawPassword = "password123";
    private final String encodedPassword = "$2a$10$encodedPassword";

    private User user;
    private CredentialsDto credentialsDto;
    private SignUpDto signUpDto;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setLogin("testUser");
        user.setPassword(encodedPassword);

        credentialsDto = new CredentialsDto();
        credentialsDto.setLogin("testUser");
        credentialsDto.setPassword(rawPassword.toCharArray());

        signUpDto = new SignUpDto();
        signUpDto.setLogin("newUser");
        signUpDto.setPassword(rawPassword.toCharArray());

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setLogin("testUser");
    }

    @Test
    void testLoginSuccessful() {
        when(userRepository.getByLogin("testUser")).thenReturn(user);
        when(passwordEncoder.matches(CharBuffer.wrap(rawPassword), encodedPassword)).thenReturn(true);
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        UserDto result = userService.login(credentialsDto);

        assertEquals(userDto.getLogin(), result.getLogin());
    }

    @Test
    void testLoginInvalidPassword() {
        when(userRepository.getByLogin("testUser")).thenReturn(user);
        when(passwordEncoder.matches(CharBuffer.wrap(rawPassword), encodedPassword)).thenReturn(false);

        AppException ex = assertThrows(AppException.class, () -> userService.login(credentialsDto));
        assertEquals("Invalid password", ex.getMessage());
    }

    @Test
    void testRegisterSuccessful() {
        when(userRepository.findByLogin("newUser")).thenReturn(Optional.empty());
        when(userMapper.signUpToUser(signUpDto)).thenReturn(user);
        when(passwordEncoder.encode(CharBuffer.wrap(rawPassword))).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        UserDto result = userService.register(signUpDto);

        assertEquals(userDto.getLogin(), result.getLogin());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegisterLoginAlreadyExists() {
        when(userRepository.findByLogin("newUser")).thenReturn(Optional.of(user));

        AppException ex = assertThrows(AppException.class, () -> userService.register(signUpDto));
        assertEquals("Login already exists", ex.getMessage());
    }

    @Test
    void testFindByLogin() {
        when(userRepository.getByLogin("testUser")).thenReturn(user);

        User result = userService.findByLogin("testUser");

        assertEquals(user.getLogin(), result.getLogin());
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(user);

        User result = userService.getUserById(1L);

        assertEquals(user.getId(), result.getId());
    }
}

