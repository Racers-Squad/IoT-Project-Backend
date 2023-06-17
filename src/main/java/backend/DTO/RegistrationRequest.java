package backend.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest {
    private String email;
    private String password;
    private String name;
    private String surname;
    private String phone;
    private Boolean isAdmin;
}
