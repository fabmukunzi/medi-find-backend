package auca.ac.medifind.service;

import auca.ac.medifind.model.ServiceArea;
import auca.ac.medifind.repository.ServiceAreaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceAreaService {

    @Autowired
    private ServiceAreaRepository serviceAreaRepository;

    public String saveServiceArea(ServiceArea serviceArea) {
        serviceAreaRepository.save(serviceArea);
        return "Service area saved successfully";
    }

    public List<ServiceArea> getAllServiceAreas() {
        return serviceAreaRepository.findAll();
    }

    public Optional<ServiceArea> getServiceAreaById(UUID id) {
        return serviceAreaRepository.findById(id);
    }

    public String updateServiceArea(UUID id, ServiceArea updated) {
        Optional<ServiceArea> existing = serviceAreaRepository.findById(id);
        if (existing.isEmpty()) {
            return "The service area with that id does not exist";
        }
        ServiceArea serviceArea = existing.get();
        serviceArea.setAreaNumber(updated.getAreaNumber());
        serviceArea.setName(updated.getName());
        serviceAreaRepository.save(serviceArea);
        return "Service area updated successfully";
    }

    public String deleteServiceArea(UUID id) {
        Optional<ServiceArea> existing = serviceAreaRepository.findById(id);
        if (existing.isEmpty()) {
            return "The service area with that id does not exist";
        }
        serviceAreaRepository.deleteById(id);
        return "Service area deleted successfully";
    }

    public List<ServiceArea> getFreeServiceAreas() {
        return serviceAreaRepository.findFreeServiceAreas();
    }
}
