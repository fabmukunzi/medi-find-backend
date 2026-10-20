package auca.ac.medifind.service;

import auca.ac.medifind.model.RequestStatus;
import auca.ac.medifind.repository.CustomerRepository;
import auca.ac.medifind.repository.DispensedItemRepository;
import auca.ac.medifind.repository.MedicineRequestRepository;
import auca.ac.medifind.repository.PharmacyRepository;
import auca.ac.medifind.repository.ServiceAreaRepository;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PharmacyRepository pharmacyRepository;

    @Autowired
    private ServiceAreaRepository serviceAreaRepository;

    @Autowired
    private MedicineRequestRepository medicineRequestRepository;

    @Autowired
    private DispensedItemRepository dispensedItemRepository;

    public Map<String, Long> getSummary() {
        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("customers", customerRepository.count());
        summary.put("pharmacies", pharmacyRepository.count());
        summary.put("freeServiceAreas", (long) serviceAreaRepository.findFreeServiceAreas().size());
        summary.put("requestsToday", medicineRequestRepository.countByRequestDate(LocalDate.now()));
        summary.put("submitted", medicineRequestRepository.countByStatus(RequestStatus.SUBMITTED));
        summary.put("dispensedItems", dispensedItemRepository.count());
        return summary;
    }
}
