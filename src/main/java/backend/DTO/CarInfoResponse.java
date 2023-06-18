package backend.DTO;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CarInfoResponse {

    private String id;
    private String carBrand;
    private String model;
    private Integer year;
    private String status;
    private Long reservation;

    public CarInfoResponse(String id, String carBrand){
        this.id = id;
        this.carBrand = carBrand;
    }

    public CarInfoResponse() {}

}
