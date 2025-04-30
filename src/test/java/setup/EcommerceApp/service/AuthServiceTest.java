package setup.EcommerceApp.service;

// --- TEST LAYER ---

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import setup.EcommerceApp.config.JWTProvider;
import setup.EcommerceApp.dto.LoginRequestDto;
import setup.EcommerceApp.dto.RegisterRequestDto;
import setup.EcommerceApp.model.User;
import setup.EcommerceApp.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTProvider jwtProvider;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_Success() {
        // Arrange
        RegisterRequestDto request = new RegisterRequestDto();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        request.setPassword("Password@123");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtProvider.generateToken(anyString())).thenReturn("dummyToken");

        // Act
        String token = authService.register(request);

        // Assert
        assertEquals("dummyToken", token);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_DuplicateEmail_ThrowsException() {
        // Arrange
        RegisterRequestDto request = new RegisterRequestDto();
        request.setEmail("existing@example.com");
        request.setPassword("Password@123");

        when(userRepository.findByEmail("existing@example.com"))
                .thenReturn(Optional.of(new User()));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> authService.register(request));
    }

    @Test
    void register_InvalidPassword_ThrowsException() {
        // Arrange
        RegisterRequestDto request = new RegisterRequestDto();
        request.setEmail("test@example.com");
        request.setPassword("weak");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> authService.register(request));
    }

    @Test
    void login_Success() {
        // Arrange
        LoginRequestDto request = new LoginRequestDto();
        request.setEmail("john@example.com");
        request.setPassword("Password@123");

        User user = new User();
        user.setEmail("john@example.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtProvider.generateToken(anyString())).thenReturn("dummyToken");

        // Act
        String token = authService.login(request);

        // Assert
        assertEquals("dummyToken", token);
    }

    @Test
    void login_InvalidPassword_ThrowsException() {
        // Arrange
        LoginRequestDto request = new LoginRequestDto();
        request.setEmail("john@example.com");
        request.setPassword("wrongPassword");

        User user = new User();
        user.setEmail("john@example.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authService.login(request));
    }

    @Test
    void login_UserNotFound_ThrowsException() {
        // Arrange
        LoginRequestDto request = new LoginRequestDto();
        request.setEmail("nonexistent@example.com");

        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> authService.login(request));
    }
}
