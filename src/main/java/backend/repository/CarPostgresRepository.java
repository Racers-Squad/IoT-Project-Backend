package backend.repository;

import backend.entity.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CarPostgresRepository extends JpaRepository<CarEntity, String> {

    @Query("from CarEntity c where c.id = :carNumber")
    CarEntity findByCarNumber(String carNumber);
}
