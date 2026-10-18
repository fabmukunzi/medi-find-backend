package auca.ac.medifind.repository;

import auca.ac.medifind.model.MedicineRequest;
import auca.ac.medifind.model.Pharmacy;
import auca.ac.medifind.model.RequestStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineRequestRepository extends JpaRepository<MedicineRequest, UUID> {

    List<MedicineRequest> findByStatusOrderByRequestDateAsc(RequestStatus status);

    List<MedicineRequest> findByRequestDateBetweenOrderByRequestDateAsc(LocalDate start, LocalDate end);

    boolean existsByPharmacyAndRequestDateAndStatusNot(Pharmacy pharmacy, LocalDate requestDate, RequestStatus status);

    @Query("SELECT mr.status, COUNT(mr) FROM MedicineRequest mr GROUP BY mr.status")
    List<Object[]> countRequestsByStatus();

    @Modifying
    @Query("UPDATE MedicineRequest mr SET mr.status = auca.ac.medifind.model.RequestStatus.DECLINED WHERE mr.pharmacy.id = :pharmacyId AND mr.requestDate = :date AND mr.status <> auca.ac.medifind.model.RequestStatus.FULFILLED")
    int cancelDay(@Param("pharmacyId") UUID pharmacyId, @Param("date") LocalDate date);

    long countByStatus(RequestStatus status);

    long countByRequestDate(LocalDate requestDate);

}
