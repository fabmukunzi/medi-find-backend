package auca.ac.medifind.service;

import auca.ac.medifind.model.Customer;
import auca.ac.medifind.repository.CustomerRepository;
import auca.ac.medifind.repository.PharmacyRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PharmacyRepository pharmacyRepository;

    public String saveCustomer(Customer customer) {
        customerRepository.save(customer);
        return "Customer saved successfully";
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(UUID id) {
        return customerRepository.findById(id);
    }

    public String updateCustomer(UUID id, Customer updated) {
        Optional<Customer> existing = customerRepository.findById(id);
        if (existing.isEmpty()) {
            return "The customer with that id does not exist";
        }
        Customer customer = existing.get();
        customer.setFirstName(updated.getFirstName());
        customer.setLastName(updated.getLastName());
        customer.setNationalId(updated.getNationalId());
        customer.setPhone(updated.getPhone());
        customerRepository.save(customer);
        return "Customer updated successfully";
    }

    public String deleteCustomer(UUID id) {
        Optional<Customer> existing = customerRepository.findById(id);
        if (existing.isEmpty()) {
            return "The customer with that id does not exist";
        }
        customerRepository.deleteById(id);
        return "Customer deleted successfully";
    }

    public List<Customer> getCustomersByLastName(String lastName) {
        return customerRepository.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }

    public boolean pharmacyExists(UUID pharmacyId) {
        return pharmacyRepository.existsById(pharmacyId);
    }

    public List<Customer> getCustomersOfPharmacy(UUID pharmacyId) {
        return customerRepository.findCustomersByPharmacyId(pharmacyId);
    }
}
