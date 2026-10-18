package auca.ac.medifind.controller;

import auca.ac.medifind.model.ServiceArea;
import auca.ac.medifind.service.ServiceAreaService;
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
@RequestMapping("/api/service-areas")
public class ServiceAreaController {

    @Autowired
    private ServiceAreaService serviceAreaService;

    @PostMapping("/save")
    public ResponseEntity<String> saveServiceArea(@RequestBody ServiceArea serviceArea) {
        String message = serviceAreaService.saveServiceArea(serviceArea);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ServiceArea>> getAllServiceAreas() {
        return ResponseEntity.ok(serviceAreaService.getAllServiceAreas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getServiceAreaById(@PathVariable UUID id) {
        Optional<ServiceArea> serviceArea = serviceAreaService.getServiceAreaById(id);
        if (serviceArea.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The service area with that id does not exist");
        }
        return ResponseEntity.ok(serviceArea.get());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateServiceArea(@PathVariable UUID id, @RequestBody ServiceArea serviceArea) {
        String message = serviceAreaService.updateServiceArea(id, serviceArea);
        if (message.equals("The service area with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteServiceArea(@PathVariable UUID id) {
        String message = serviceAreaService.deleteServiceArea(id);
        if (message.equals("The service area with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        return ResponseEntity.ok(message);
    }

    @GetMapping("/free")
    public ResponseEntity<List<ServiceArea>> getFreeServiceAreas() {
        return ResponseEntity.ok(serviceAreaService.getFreeServiceAreas());
    }
}
