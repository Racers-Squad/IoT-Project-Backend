package backend.services;

import backend.entity.CarParameters;
import backend.repository.CarMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MongoSaver {

    @Autowired
    private CarMongoRepository carMongoRepository;

    private void beforeSave(CarParameters carParameters) {
        Optional<CarParameters> maybeParams = carMongoRepository.findActualValueByCarId(carParameters.getCarId());
        if (maybeParams.isPresent()) {
            CarParameters prevParams = maybeParams.get();
            prevParams.setValidTo(carParameters.getValidFrom());
            carMongoRepository.save(prevParams);;
        }
    }

    public void save(CarParameters carParameters) {
        beforeSave(carParameters);
        carMongoRepository.save(carParameters);
    }

    public CarParameters readActualParameters(String carId) {
        return carRepository.findActualValueByCarId(carId).get();
    }

}
