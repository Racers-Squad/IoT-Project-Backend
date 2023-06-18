package backend.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReservationInfoResponse {

    private Long id;
    private UserInfoResponse user;
    private CarInfoResponse car;
    private String startTime;
    private String endTime;

}
