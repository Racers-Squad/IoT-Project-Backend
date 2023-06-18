package backend.repository;

import backend.entity.TripEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<TripEntity, Long> {

    List<TripEntity> findAll();

    List<TripEntity> findAllByCarId(String carId);

    List<TripEntity> findAllByDriverId(Integer driverId);

    @Query("from TripEntity e " +
            "where e.carId = :carId " +
            "and e.endTime is null")
    TripEntity findCurrentTrip(String carId);
}
