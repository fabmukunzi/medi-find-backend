package auca.ac.medifind.repository;

import auca.ac.medifind.model.Pharmacy;
import auca.ac.medifind.model.ServiceArea;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PharmacyRepository extends JpaRepository<Pharmacy, UUID> {

    @Query("SELECT p FROM Pharmacy p JOIN p.medicineClasses mc WHERE LOWER(mc.name) = LOWER(:name)")
    List<Pharmacy> findByMedicineClassNameIgnoreCase(@Param("name") String name);

    boolean existsByServiceArea(ServiceArea serviceArea);

}
