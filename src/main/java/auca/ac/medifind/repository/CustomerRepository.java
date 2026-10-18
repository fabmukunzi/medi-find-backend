package auca.ac.medifind.repository;

import auca.ac.medifind.model.Customer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    List<Customer> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);

    @Query("SELECT DISTINCT mr.customer FROM MedicineRequest mr WHERE mr.pharmacy.id = :pharmacyId")
    List<Customer> findCustomersByPharmacyId(@Param("pharmacyId") UUID pharmacyId);

    Optional<Customer> findByNationalId(String nationalId);

}
