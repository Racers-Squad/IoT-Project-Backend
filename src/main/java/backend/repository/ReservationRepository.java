package backend.repository;

import backend.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    List<ReservationEntity> findByCarId(String carId);

    List<ReservationEntity> findByDriverId(Long driverId);

    @Query("from ReservationEntity e where " +
            "e.driverId = :driverId " +
            "and e.startTime > :startTime " +
            "and e.endTime is not null " +
            "and e.endTime < :endTime")
    Optional<ReservationEntity> findByDriverAndTimeBetween(Long driverId, Date startTime, Date endTime);

}
