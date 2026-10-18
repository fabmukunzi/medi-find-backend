package auca.ac.medifind.service;

import auca.ac.medifind.model.MedicineClass;
import auca.ac.medifind.repository.MedicineClassRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicineClassService {

    @Autowired
    private MedicineClassRepository medicineClassRepository;

    public String saveMedicineClass(MedicineClass medicineClass) {
        medicineClassRepository.save(medicineClass);
        return "Medicine class saved successfully";
    }

    public List<MedicineClass> getAllMedicineClasses() {
        return medicineClassRepository.findAll();
    }

    public Optional<MedicineClass> getMedicineClassById(UUID id) {
        return medicineClassRepository.findById(id);
    }

    public String updateMedicineClass(UUID id, MedicineClass updated) {
        Optional<MedicineClass> existing = medicineClassRepository.findById(id);
        if (existing.isEmpty()) {
            return "The medicine class with that id does not exist";
        }
        MedicineClass medicineClass = existing.get();
        medicineClass.setName(updated.getName());
        medicineClassRepository.save(medicineClass);
        return "Medicine class updated successfully";
    }

    public String deleteMedicineClass(UUID id) {
        Optional<MedicineClass> existing = medicineClassRepository.findById(id);
        if (existing.isEmpty()) {
            return "The medicine class with that id does not exist";
        }
        medicineClassRepository.deleteById(id);
        return "Medicine class deleted successfully";
    }

    public List<MedicineClass> getUnusedMedicineClasses() {
        return medicineClassRepository.findUnusedMedicineClasses();
    }
}
