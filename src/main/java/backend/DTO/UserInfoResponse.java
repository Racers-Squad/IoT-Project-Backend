package backend.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserInfoResponse {

    private Long id;
    private String name;
    private String surname;
    private String phone;
    private String email;

}
