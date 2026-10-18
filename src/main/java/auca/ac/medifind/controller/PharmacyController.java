package auca.ac.medifind.controller;

import auca.ac.medifind.model.Pharmacy;
import auca.ac.medifind.service.PharmacyService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pharmacies")
public class PharmacyController {

    @Autowired
    private PharmacyService pharmacyService;

    @PostMapping("/save")
    public ResponseEntity<String> savePharmacy(@RequestBody Pharmacy pharmacy) {
        String message = pharmacyService.savePharmacy(pharmacy);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Pharmacy>> getAllPharmacies() {
        return ResponseEntity.ok(pharmacyService.getAllPharmacies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPharmacyById(@PathVariable UUID id) {
        Optional<Pharmacy> pharmacy = pharmacyService.getPharmacyById(id);
        if (pharmacy.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The pharmacy with that id does not exist");
        }
        return ResponseEntity.ok(pharmacy.get());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updatePharmacy(@PathVariable UUID id, @RequestBody Pharmacy pharmacy) {
        String message = pharmacyService.updatePharmacy(id, pharmacy);
        if (message.equals("The pharmacy with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePharmacy(@PathVariable UUID id) {
        String message = pharmacyService.deletePharmacy(id);
        if (message.equals("The pharmacy with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        return ResponseEntity.ok(message);
    }

    @GetMapping("/by-medicine-class")
    public ResponseEntity<List<Pharmacy>> getPharmaciesByMedicineClass(@RequestParam String name) {
        return ResponseEntity.ok(pharmacyService.getPharmaciesByMedicineClass(name));
    }

    @PutMapping("/{pharmacyId}/medicine-classes/{medicineClassId}")
    public ResponseEntity<String> addMedicineClass(
            @PathVariable UUID pharmacyId,
            @PathVariable UUID medicineClassId) {
        String message = pharmacyService.addMedicineClassToPharmacy(pharmacyId, medicineClassId);
        if (message.equals("The pharmacy with that id does not exist")
                || message.equals("That medicine class does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else if (message.equals("The pharmacy already has that medicine class")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        }
        return ResponseEntity.ok(message);
    }

    @PatchMapping("/{pharmacyId}/service-area")
    public ResponseEntity<String> changeServiceArea(
            @PathVariable UUID pharmacyId,
            @RequestParam int areaNumber) {
        String message = pharmacyService.changeServiceArea(pharmacyId, areaNumber);
        if (message.equals("The pharmacy with that id does not exist")
                || message.equals("The service area with that areaNumber does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else if (message.equals("That service area is taken by another pharmacy")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        }
        return ResponseEntity.ok(message);
    }
}
