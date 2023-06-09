package backend.entity;

import backend.services.cars.CarParameter;
import jakarta.persistence.GeneratedValue;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

public class CarParameters {

    @Id
    @GeneratedValue
    private String id;

    private String carId;

    private Date validFrom;
    private Date validTo;

    private Map<CarParameter, Object> parameters;

    public CarParameters() {
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getCarId() {
        return carId;
    }


    public Map<CarParameter, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<CarParameter, Object> parameters) {
        this.parameters = parameters;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
