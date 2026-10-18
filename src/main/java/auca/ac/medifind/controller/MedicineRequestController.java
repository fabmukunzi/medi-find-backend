package auca.ac.medifind.controller;

import auca.ac.medifind.model.MedicineRequest;
import auca.ac.medifind.model.RequestStatus;
import auca.ac.medifind.service.MedicineRequestService;
import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/medicine-requests")
public class MedicineRequestController {

    @Autowired
    private MedicineRequestService medicineRequestService;

    @PostMapping("/save")
    public ResponseEntity<String> saveMedicineRequest(@RequestBody MedicineRequest medicineRequest) {
        String message = medicineRequestService.saveMedicineRequest(medicineRequest);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<MedicineRequest>> getAllMedicineRequests() {
        return ResponseEntity.ok(medicineRequestService.getAllMedicineRequests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMedicineRequestById(@PathVariable UUID id) {
        Optional<MedicineRequest> medicineRequest = medicineRequestService.getMedicineRequestById(id);
        if (medicineRequest.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The medicine request with that id does not exist");
        }
        return ResponseEntity.ok(medicineRequest.get());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateMedicineRequest(@PathVariable UUID id, @RequestBody MedicineRequest medicineRequest) {
        String message = medicineRequestService.updateMedicineRequest(id, medicineRequest);
        if (message.equals("The medicine request with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteMedicineRequest(@PathVariable UUID id) {
        String message = medicineRequestService.deleteMedicineRequest(id);
        if (message.equals("The medicine request with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        return ResponseEntity.ok(message);
    }

    @GetMapping("/by-status")
    public ResponseEntity<List<MedicineRequest>> getRequestsByStatus(@RequestParam RequestStatus status) {
        return ResponseEntity.ok(medicineRequestService.getRequestsByStatus(status));
    }

    @GetMapping("/between")
    public ResponseEntity<List<MedicineRequest>> getRequestsBetween(
            @RequestParam String start,
            @RequestParam String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        return ResponseEntity.ok(medicineRequestService.getRequestsBetweenDates(startDate, endDate));
    }
}
