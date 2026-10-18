package auca.ac.medifind.repository;

import auca.ac.medifind.model.ServiceArea;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceAreaRepository extends JpaRepository<ServiceArea, UUID> {

    @Query("SELECT sa FROM ServiceArea sa WHERE NOT EXISTS (SELECT p FROM Pharmacy p WHERE p.serviceArea = sa) ORDER BY sa.areaNumber ASC")
    List<ServiceArea> findFreeServiceAreas();

    Optional<ServiceArea> findByAreaNumber(int areaNumber);

}
