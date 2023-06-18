package backend.DTO;

import backend.entity.CarParameters;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TotalCarInfoResponse {

    private CarInfoResponse carInfoResponse;
    private CarParameters carParameters;

}
