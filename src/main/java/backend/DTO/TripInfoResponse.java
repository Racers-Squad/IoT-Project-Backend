package backend.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TripInfoResponse {

    private Long id;
    private UserInfoResponse user;
    private CarInfoResponse car;
    private String startTime;
    private String endTime;
    private String startLocation;
    private String endLocation;

    private Double duration;

}
