package auca.ac.medifind.service;

import auca.ac.medifind.model.DispensedItem;
import auca.ac.medifind.model.MedicineRequest;
import auca.ac.medifind.model.RequestStatus;
import auca.ac.medifind.repository.DispensedItemRepository;
import auca.ac.medifind.repository.MedicineRequestRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DispensedItemService {

    @Autowired
    private DispensedItemRepository dispensedItemRepository;

    @Autowired
    private MedicineRequestRepository medicineRequestRepository;

    public String saveDispensedItem(UUID medicineRequestId, DispensedItem item) {
        Optional<MedicineRequest> reqOpt = medicineRequestRepository.findById(medicineRequestId);
        if (reqOpt.isEmpty()) {
            return "The medicine request with that id does not exist";
        }

        MedicineRequest request = reqOpt.get();
        if (request.getStatus() != RequestStatus.FULFILLED) {
            return "Dispensed items are only for FULFILLED medicine requests";
        }

        item.setDispensedDate(LocalDate.now());
        item.setMedicineRequest(request);
        dispensedItemRepository.save(item);
        return "Dispensed item saved successfully";
    }

    public List<DispensedItem> getItemsByCustomer(String nationalId) {
        return dispensedItemRepository.findByCustomerNationalId(nationalId);
    }

    public String deleteDispensedItem(UUID id) {
        Optional<DispensedItem> existing = dispensedItemRepository.findById(id);
        if (existing.isEmpty()) {
            return "The dispensed item with that id does not exist";
        }
        dispensedItemRepository.deleteById(id);
        return "Dispensed item deleted successfully";
    }

    public List<DispensedItem> getAllDispensedItems() {
        return dispensedItemRepository.findAll();
    }
}
