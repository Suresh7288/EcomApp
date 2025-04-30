package setup.EcommerceApp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import setup.EcommerceApp.dto.AuthResponseDto;
import setup.EcommerceApp.dto.LoginRequestDto;
import setup.EcommerceApp.dto.RegisterRequestDto;
import setup.EcommerceApp.model.Role;
import setup.EcommerceApp.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final AuthService authService;
    public UserController(AuthService authService){
        this.authService=authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody RegisterRequestDto request) {
        String token = authService.register(request);
        return ResponseEntity.ok(new AuthResponseDto(token, request.getEmail(), Role.CUSTOMER));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto request) {
        String token = authService.login(request);
        return ResponseEntity.ok(new AuthResponseDto(token, request.getEmail(), null));
    }
}
