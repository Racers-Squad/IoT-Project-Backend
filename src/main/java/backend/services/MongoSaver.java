package backend.services;

import backend.entity.CarParameters;
import backend.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MongoSaver {

    @Autowired
    private CarRepository carRepository;

    private void beforeSave(CarParameters carParameters) {
        Optional<CarParameters> maybeParams = carRepository.findActualValueByCarId(carParameters.getCarId());
        if (maybeParams.isPresent()) {
            CarParameters prevParams = maybeParams.get();
            prevParams.setValidTo(carParameters.getValidFrom());
            carRepository.save(prevParams);;
        }
    }

    public void save(CarParameters carParameters) {
        beforeSave(carParameters);
        carRepository.save(carParameters);
    }

}
