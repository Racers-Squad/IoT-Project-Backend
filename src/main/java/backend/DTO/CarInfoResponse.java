package backend.DTO;

public class CarInfoResponse {

    private int id;

    private String carNumber;

    private String carBrand;

    public CarInfoResponse(int id, String carNumber, String carBrand){
        this.id = id;
        this.carNumber = carNumber;
        this.carBrand = carBrand;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }
}
