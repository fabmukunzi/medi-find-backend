package auca.ac.medifind.controller;

import auca.ac.medifind.model.DispensedItem;
import auca.ac.medifind.service.DispensedItemService;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dispensed-items")
public class DispensedItemController {

    @Autowired
    private DispensedItemService dispensedItemService;

    @PostMapping("/save")
    public ResponseEntity<String> saveDispensedItem(
            @RequestParam UUID medicineRequestId,
            @RequestBody DispensedItem item) {
        String message = dispensedItemService.saveDispensedItem(medicineRequestId, item);
        if (message.equals("The medicine request with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else if (message.equals("Dispensed items are only for FULFILLED medicine requests")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/customer/{nationalId}")
    public ResponseEntity<List<DispensedItem>> getItemsByCustomer(@PathVariable String nationalId) {
        return ResponseEntity.ok(dispensedItemService.getItemsByCustomer(nationalId));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDispensedItem(@PathVariable UUID id) {
        String message = dispensedItemService.deleteDispensedItem(id);
        if (message.equals("The dispensed item with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        return ResponseEntity.ok(message);
    }

    @GetMapping("/all")
    public ResponseEntity<List<DispensedItem>> getAllDispensedItems() {
        return ResponseEntity.ok(dispensedItemService.getAllDispensedItems());
    }
}
