package auca.ac.medifind.controller;

import auca.ac.medifind.model.MedicineClass;
import auca.ac.medifind.service.MedicineClassService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/medicine-classes")
public class MedicineClassController {

    @Autowired
    private MedicineClassService medicineClassService;

    @PostMapping("/save")
    public ResponseEntity<String> saveMedicineClass(@RequestBody MedicineClass medicineClass) {
        String message = medicineClassService.saveMedicineClass(medicineClass);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<MedicineClass>> getAllMedicineClasses() {
        return ResponseEntity.ok(medicineClassService.getAllMedicineClasses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMedicineClassById(@PathVariable UUID id) {
        Optional<MedicineClass> medicineClass = medicineClassService.getMedicineClassById(id);
        if (medicineClass.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The medicine class with that id does not exist");
        }
        return ResponseEntity.ok(medicineClass.get());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateMedicineClass(@PathVariable UUID id, @RequestBody MedicineClass medicineClass) {
        String message = medicineClassService.updateMedicineClass(id, medicineClass);
        if (message.equals("The medicine class with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteMedicineClass(@PathVariable UUID id) {
        String message = medicineClassService.deleteMedicineClass(id);
        if (message.equals("The medicine class with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        return ResponseEntity.ok(message);
    }

    @GetMapping("/unused")
    public ResponseEntity<List<MedicineClass>> getUnusedMedicineClasses() {
        return ResponseEntity.ok(medicineClassService.getUnusedMedicineClasses());
    }
}
