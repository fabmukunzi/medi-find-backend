package auca.ac.medifind.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "dispensed_item")
public class DispensedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "medicine_name")
    private String medicineName;

    @Column(name = "dosage")
    private String dosage;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "dispensed_date")
    private LocalDate dispensedDate;

    @ManyToOne
    @JoinColumn(name = "medicine_request_id")
    private MedicineRequest medicineRequest;

    public DispensedItem() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getDispensedDate() {
        return dispensedDate;
    }

    public void setDispensedDate(LocalDate dispensedDate) {
        this.dispensedDate = dispensedDate;
    }

    public MedicineRequest getMedicineRequest() {
        return medicineRequest;
    }

    public void setMedicineRequest(MedicineRequest medicineRequest) {
        this.medicineRequest = medicineRequest;
    }
}
