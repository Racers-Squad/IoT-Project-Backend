package backend.DTO;

public class AddCarRequest {

    private String id;

    private String carBrand;

    private String model;

    private Integer year;


    public String getId() {
        return id;
    }

    public void setCarNumber(String id) {
        this.id = id;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }
}
