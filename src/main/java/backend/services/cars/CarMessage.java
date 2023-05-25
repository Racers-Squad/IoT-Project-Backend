package backend.services.cars;

import java.util.Date;
import java.util.Map;

public class CarMessage {

    private String carId;
    private Map<Long, Object> values;
    private Date validFrom;

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public Map<Long, Object> getValues() {
        return values;
    }

    public void setValues(Map<Long, Object> values) {
        this.values = values;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }
}
