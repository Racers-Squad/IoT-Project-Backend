package backend.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarInfoResponse {

    private int id;

    private String carNumber;
    private String carBrand;
    private String model;
    private Integer year;
    private String status;

    public CarInfoResponse(int id, String carNumber, String carBrand){
        this.id = id;
        this.carNumber = carNumber;
        this.carBrand = carBrand;
    }

    public CarInfoResponse() {}

}
