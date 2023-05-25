package backend.services.cars;

import backend.entity.CarParameters;

import java.util.Date;
import java.util.Map;

public interface CarParamsResolver {

    CarParameters convertToGlobal(String carId, Date validFrom, Map<Long, Object> values);

}
