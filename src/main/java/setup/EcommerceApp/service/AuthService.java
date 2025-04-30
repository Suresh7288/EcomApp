package setup.EcommerceApp.service;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import setup.EcommerceApp.config.JWTProvider;
import setup.EcommerceApp.dto.LoginRequestDto;
import setup.EcommerceApp.dto.RegisterRequestDto;
import setup.EcommerceApp.model.Role;
import setup.EcommerceApp.model.User;
import setup.EcommerceApp.repository.UserRepository;

import java.time.LocalDateTime;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final  PasswordEncoder passwordEncoder;
    private final JWTProvider jwtProvider;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTProvider jwtProvider){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }
    @Transactional
    public String register(RegisterRequestDto request){
        //Valid password Strength
        if(!isValidPassword(request.getPassword())){
            throw new IllegalArgumentException("Password must be 8 character long and include special characters and numbers");
        }
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new IllegalArgumentException(("Email already exist, try with another email"));
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(Role.CUSTOMER);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        return jwtProvider.generateToken(user.getEmail());

    }

    public String login(LoginRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

          return jwtProvider.generateToken(user.getEmail());
    }
    // Password validation utility
    private boolean isValidPassword(String password) {
        return password.length() >= 8 &&
                password.matches(".*[!@#$%^&*()].*") &&
                password.matches(".*\\d.*");
    }

    // Admin Pre-seed logic (Optional run once)
    @PostConstruct
    public void initAdmin() {
        if (userRepository.findByEmail("admin@ecommerce.com").isEmpty()) {
            User admin = new User();
            admin.setName("Admin");
            admin.setEmail("admin@ecommerce.com");
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setRole(Role.ADMIN);
            admin.setCreatedAt(LocalDateTime.now());
            userRepository.save(admin);
        }
    }

}
