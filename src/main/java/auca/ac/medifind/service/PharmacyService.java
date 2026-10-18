package auca.ac.medifind.service;

import auca.ac.medifind.model.MedicineClass;
import auca.ac.medifind.model.Pharmacy;
import auca.ac.medifind.model.ServiceArea;
import auca.ac.medifind.repository.MedicineClassRepository;
import auca.ac.medifind.repository.PharmacyRepository;
import auca.ac.medifind.repository.ServiceAreaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PharmacyService {

    @Autowired
    private PharmacyRepository pharmacyRepository;

    @Autowired
    private MedicineClassRepository medicineClassRepository;

    @Autowired
    private ServiceAreaRepository serviceAreaRepository;

    public String savePharmacy(Pharmacy pharmacy) {
        pharmacyRepository.save(pharmacy);
        return "Pharmacy saved successfully";
    }

    public List<Pharmacy> getAllPharmacies() {
        return pharmacyRepository.findAll();
    }

    public Optional<Pharmacy> getPharmacyById(UUID id) {
        return pharmacyRepository.findById(id);
    }

    public String updatePharmacy(UUID id, Pharmacy updated) {
        Optional<Pharmacy> existing = pharmacyRepository.findById(id);
        if (existing.isEmpty()) {
            return "The pharmacy with that id does not exist";
        }
        Pharmacy pharmacy = existing.get();
        pharmacy.setName(updated.getName());
        pharmacy.setStreet(updated.getStreet());
        pharmacy.setPhone(updated.getPhone());
        if (updated.getServiceArea() != null) {
            pharmacy.setServiceArea(updated.getServiceArea());
        }
        if (updated.getMedicineClasses() != null) {
            pharmacy.setMedicineClasses(updated.getMedicineClasses());
        }
        pharmacyRepository.save(pharmacy);
        return "Pharmacy updated successfully";
    }

    public String deletePharmacy(UUID id) {
        Optional<Pharmacy> existing = pharmacyRepository.findById(id);
        if (existing.isEmpty()) {
            return "The pharmacy with that id does not exist";
        }
        pharmacyRepository.deleteById(id);
        return "Pharmacy deleted successfully";
    }

    public List<Pharmacy> getPharmaciesByMedicineClass(String name) {
        return pharmacyRepository.findByMedicineClassNameIgnoreCase(name);
    }

    public String addMedicineClassToPharmacy(UUID pharmacyId, UUID medicineClassId) {
        Optional<Pharmacy> pharmacyOpt = pharmacyRepository.findById(pharmacyId);
        if (pharmacyOpt.isEmpty()) {
            return "The pharmacy with that id does not exist";
        }
        Optional<MedicineClass> medicineClassOpt = medicineClassRepository.findById(medicineClassId);
        if (medicineClassOpt.isEmpty()) {
            return "That medicine class does not exist";
        }
        Pharmacy pharmacy = pharmacyOpt.get();
        MedicineClass medicineClass = medicineClassOpt.get();

        for (MedicineClass mc : pharmacy.getMedicineClasses()) {
            if (mc.getId().equals(medicineClassId)) {
                return "The pharmacy already has that medicine class";
            }
        }

        pharmacy.getMedicineClasses().add(medicineClass);
        pharmacyRepository.save(pharmacy);
        return "Medicine class added to the pharmacy";
    }

    public String changeServiceArea(UUID pharmacyId, int areaNumber) {
        Optional<Pharmacy> pharmacyOpt = pharmacyRepository.findById(pharmacyId);
        if (pharmacyOpt.isEmpty()) {
            return "The pharmacy with that id does not exist";
        }
        Optional<ServiceArea> serviceAreaOpt = serviceAreaRepository.findByAreaNumber(areaNumber);
        if (serviceAreaOpt.isEmpty()) {
            return "The service area with that areaNumber does not exist";
        }

        ServiceArea newArea = serviceAreaOpt.get();
        if (pharmacyRepository.existsByServiceArea(newArea)) {
            return "That service area is taken by another pharmacy";
        }

        Pharmacy pharmacy = pharmacyOpt.get();
        pharmacy.setServiceArea(newArea);
        pharmacyRepository.save(pharmacy);
        return "Service area changed successfully";
    }
}
