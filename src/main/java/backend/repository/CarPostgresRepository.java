package backend.repository;

import backend.entity.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarPostgresRepository extends JpaRepository<CarEntity, String> {

    CarEntity findByCarNumber(String carNumber);
}
