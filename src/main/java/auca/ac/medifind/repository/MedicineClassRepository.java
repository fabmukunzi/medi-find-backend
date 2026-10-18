package auca.ac.medifind.repository;

import auca.ac.medifind.model.MedicineClass;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineClassRepository extends JpaRepository<MedicineClass, UUID> {

    @Query("SELECT mc FROM MedicineClass mc WHERE mc.pharmacies IS EMPTY")
    List<MedicineClass> findUnusedMedicineClasses();

}
