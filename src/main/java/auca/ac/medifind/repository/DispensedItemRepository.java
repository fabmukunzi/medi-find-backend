package auca.ac.medifind.repository;

import auca.ac.medifind.model.DispensedItem;
import auca.ac.medifind.model.MedicineRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DispensedItemRepository extends JpaRepository<DispensedItem, UUID> {

    @Query("SELECT d FROM DispensedItem d WHERE d.medicineRequest.customer.nationalId = :nationalId ORDER BY d.dispensedDate DESC")
    List<DispensedItem> findByCustomerNationalId(@Param("nationalId") String nationalId);

    boolean existsByMedicineRequest(MedicineRequest medicineRequest);

}
