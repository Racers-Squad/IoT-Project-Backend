package backend.services.cars;

import backend.entity.CarParameters;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static backend.services.cars.CarParameter.*;

public class ZhiguliParamsResolver implements CarParamsResolver {

    private static final Map<Long, CarParameter> parameterMappings = new HashMap<>();

    {{
        parameterMappings.put(1L, DOORS_LOCKED);
        parameterMappings.put(2L, START_STATUS);
        parameterMappings.put(3L, PETROL_LEVEL);
        parameterMappings.put(4L, OUT_TEMPERATURE);
        parameterMappings.put(5L, ENGINE_RPM);
    }}

    public CarParameters convertToGlobal(String carId, Date validFrom, Map<Long, Object> values) {
        Map<CarParameter, Object> carParameterObjectMap = new HashMap<>();
        for (Map.Entry<Long, Object> entry : values.entrySet()) {
            carParameterObjectMap.put(parameterMappings.get(entry.getKey()), entry.getValue());
        }
        CarParameters carParameters = new CarParameters();
        carParameters.setParameters(carParameterObjectMap);
        carParameters.setCarId(carId);
        carParameters.setValidFrom(validFrom);
        carParameters.setValidTo(null);

        return carParameters;
    }

}
