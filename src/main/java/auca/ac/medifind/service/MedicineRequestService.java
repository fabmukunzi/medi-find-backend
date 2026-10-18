package auca.ac.medifind.service;

import auca.ac.medifind.model.MedicineRequest;
import auca.ac.medifind.model.RequestStatus;
import auca.ac.medifind.repository.MedicineRequestRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicineRequestService {

    @Autowired
    private MedicineRequestRepository medicineRequestRepository;

    public String saveMedicineRequest(MedicineRequest medicineRequest) {
        if (medicineRequest.getStatus() == null) {
            medicineRequest.setStatus(RequestStatus.SUBMITTED);
        }
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
        request.setStatus(updated.getStatus());
        if (updated.getCustomer() != null) {
            request.setCustomer(updated.getCustomer());
        }
        if (updated.getPharmacy() != null) {
            request.setPharmacy(updated.getPharmacy());
        }
        medicineRequestRepository.save(request);
        return "Medicine request updated successfully";
    }

    public String deleteMedicineRequest(UUID id) {
        Optional<MedicineRequest> existing = medicineRequestRepository.findById(id);
        if (existing.isEmpty()) {
            return "The medicine request with that id does not exist";
        }
        medicineRequestRepository.deleteById(id);
        return "Medicine request deleted successfully";
    }
}
