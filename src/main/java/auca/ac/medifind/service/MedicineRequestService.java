package auca.ac.medifind.service;

import auca.ac.medifind.model.MedicineRequest;
import auca.ac.medifind.model.RequestStatus;
import auca.ac.medifind.repository.MedicineRequestRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicineRequestService {

    @Autowired
    private MedicineRequestRepository medicineRequestRepository;

    @Autowired
    private auca.ac.medifind.repository.DispensedItemRepository dispensedItemRepository;

    public String saveMedicineRequest(MedicineRequest medicineRequest) {
        if (medicineRequest.getRequestDate() != null && medicineRequest.getRequestDate().isBefore(LocalDate.now())) {
            return "Medicine request date cannot be in the past";
        }
        if (medicineRequest.getPharmacy() != null && medicineRequest.getRequestDate() != null) {
            boolean exists = medicineRequestRepository.existsByPharmacyAndRequestDateAndStatusNot(
                    medicineRequest.getPharmacy(), medicineRequest.getRequestDate(), RequestStatus.DECLINED);
            if (exists) {
                return "Pharmacy already has a request on that date";
            }
        }
        medicineRequest.setStatus(RequestStatus.SUBMITTED);
        medicineRequestRepository.save(medicineRequest);
        return "Medicine request saved successfully";
    }

    public List<MedicineRequest> getAllMedicineRequests() {
        return medicineRequestRepository.findAll();
    }

    public Optional<MedicineRequest> getMedicineRequestById(UUID id) {
        return medicineRequestRepository.findById(id);
    }

    public String updateMedicineRequest(UUID id, MedicineRequest updated) {
        Optional<MedicineRequest> existing = medicineRequestRepository.findById(id);
        if (existing.isEmpty()) {
            return "The medicine request with that id does not exist";
        }
        MedicineRequest request = existing.get();
        request.setRequestDate(updated.getRequestDate());
        request.setMedicineName(updated.getMedicineName());
        // General PUT update must no longer change the status; it only changes via status flow
        if (updated.getCustomer() != null) {
            request.setCustomer(updated.getCustomer());
        }
        if (updated.getPharmacy() != null) {
            request.setPharmacy(updated.getPharmacy());
        }
        medicineRequestRepository.save(request);
        return "Medicine request updated successfully";
    }

    public String updateStatus(UUID id, RequestStatus newStatus) {
        Optional<MedicineRequest> existing = medicineRequestRepository.findById(id);
        if (existing.isEmpty()) {
            return "The medicine request with that id does not exist";
        }
        MedicineRequest request = existing.get();
        RequestStatus currentStatus = request.getStatus();

        boolean allowed = false;
        if (currentStatus == RequestStatus.SUBMITTED) {
            if (newStatus == RequestStatus.MATCHED || newStatus == RequestStatus.DECLINED) {
                allowed = true;
            }
        } else if (currentStatus == RequestStatus.MATCHED) {
            if (newStatus == RequestStatus.FULFILLED || newStatus == RequestStatus.DECLINED) {
                allowed = true;
            }
        }

        if (!allowed) {
            return "Cannot change status from " + currentStatus + " to " + newStatus;
        }

        request.setStatus(newStatus);
        medicineRequestRepository.save(request);
        return "Medicine request status updated successfully";
    }

    public String deleteMedicineRequest(UUID id) {
        Optional<MedicineRequest> existing = medicineRequestRepository.findById(id);
        if (existing.isEmpty()) {
            return "The medicine request with that id does not exist";
        }
        if (dispensedItemRepository.existsByMedicineRequest(existing.get())) {
            return "That medicine request has dispensed items, delete them first";
        }
        medicineRequestRepository.deleteById(id);
        return "Medicine request deleted successfully";
    }

    public List<MedicineRequest> getRequestsByStatus(RequestStatus status) {
        return medicineRequestRepository.findByStatusOrderByRequestDateAsc(status);
    }

    public List<MedicineRequest> getRequestsBetweenDates(LocalDate start, LocalDate end) {
        return medicineRequestRepository.findByRequestDateBetweenOrderByRequestDateAsc(start, end);
    }
}
