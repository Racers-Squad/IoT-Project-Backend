package backend.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarInfoResponse {


    private String id;
    private String carBrand;
    private String model;
    private Integer year;
    private String status;

    public CarInfoResponse(String id, String carBrand){
        this.id = id;
        this.carBrand = carBrand;
    }

    public CarInfoResponse() {}

}
