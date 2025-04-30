package setup.EcommerceApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import setup.EcommerceApp.model.Role;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {
    private String token;
    private String email;
    private Role role;
}
