package auca.ac.medifind.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pharmacy")
public class Pharmacy {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "street")
    private String street;

    @Column(name = "phone")
    private String phone;

    @ManyToOne
    @JoinColumn(name = "service_area_id", nullable = true)
    private ServiceArea serviceArea;

    @ManyToMany
    @JoinTable(
        name = "pharmacy_medicine_class",
        joinColumns = @JoinColumn(name = "pharmacy_id"),
        inverseJoinColumns = @JoinColumn(name = "medicine_class_id")
    )
    private List<MedicineClass> medicineClasses = new ArrayList<>();

    public Pharmacy() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ServiceArea getServiceArea() {
        return serviceArea;
    }

    public void setServiceArea(ServiceArea serviceArea) {
        this.serviceArea = serviceArea;
    }

    public List<MedicineClass> getMedicineClasses() {
        return medicineClasses;
    }

    public void setMedicineClasses(List<MedicineClass> medicineClasses) {
        this.medicineClasses = medicineClasses;
    }
}
