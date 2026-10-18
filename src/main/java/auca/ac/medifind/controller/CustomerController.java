package auca.ac.medifind.controller;

import auca.ac.medifind.model.Customer;
import auca.ac.medifind.service.CustomerService;
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
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/save")
    public ResponseEntity<String> saveCustomer(@RequestBody Customer customer) {
        String message = customerService.saveCustomer(customer);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable UUID id) {
        Optional<Customer> customer = customerService.getCustomerById(id);
        if (customer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The customer with that id does not exist");
        }
        return ResponseEntity.ok(customer.get());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCustomer(@PathVariable UUID id, @RequestBody Customer customer) {
        String message = customerService.updateCustomer(id, customer);
        if (message.equals("The customer with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable UUID id) {
        String message = customerService.deleteCustomer(id);
        if (message.equals("The customer with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        return ResponseEntity.ok(message);
    }

    @GetMapping("/by-last-name")
    public ResponseEntity<List<Customer>> getCustomersByLastName(@RequestParam String lastName) {
        return ResponseEntity.ok(customerService.getCustomersByLastName(lastName));
    }

    @GetMapping("/of-pharmacy/{pharmacyId}")
    public ResponseEntity<?> getCustomersOfPharmacy(@PathVariable UUID pharmacyId) {
        if (!customerService.pharmacyExists(pharmacyId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The pharmacy with that id does not exist");
        }
        return ResponseEntity.ok(customerService.getCustomersOfPharmacy(pharmacyId));
    }
}
