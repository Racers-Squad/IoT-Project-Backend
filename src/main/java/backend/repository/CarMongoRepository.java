package backend.repository;

import backend.entity.CarParameters;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CarMongoRepository extends MongoRepository<CarParameters, Long> {

    List<CarParameters> findByCarId(String id);

    @Query("{'validTo':  null, 'carId':  ?0}")
    Optional<CarParameters> findActualValueByCarId(String id);

}