package com.businessmanagement.exports.entity;

import com.businessmanagement.master.entity.AppUser;
import com.businessmanagement.vehicles.entity.Vehicle;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "export_delivery")
public class ExportDelivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "export_id", nullable = false)
    private ExportTicket exportTicket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Column(name = "truck_license_plate", length = 30)
    private String truckLicensePlate;

    @Column(name = "loading_warehouse", length = 255)
    private String loadingWarehouse;

    @Column(name = "import_warehouse", length = 255)
    private String importWarehouse;

    @Column(name = "delivered_volume", nullable = false, precision = 14, scale = 3)
    private BigDecimal deliveredVolume;

    @Column(name = "delivery_date", nullable = false)
    private LocalDate deliveryDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DeliveryStatus status;

    @Column(length = 1000)
    private String note;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    private AppUser createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected ExportDelivery() {
    }

    public ExportDelivery(ExportTicket exportTicket, BigDecimal deliveredVolume,
                           LocalDate deliveryDate, String note, AppUser createdBy) {
        this.exportTicket = exportTicket;
        this.deliveredVolume = deliveredVolume;
        this.deliveryDate = deliveryDate;
        this.status = DeliveryStatus.CONFIRMED;
        this.note = note;
        this.createdBy = createdBy;
    }

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Vehicle getVehicle() { return vehicle; }
    public String getTruckLicensePlate() { return truckLicensePlate; }
    public String getLoadingWarehouse() { return loadingWarehouse; }
    public String getImportWarehouse() { return importWarehouse; }
    public BigDecimal getDeliveredVolume() { return deliveredVolume; }
    public LocalDate getDeliveryDate() { return deliveryDate; }
    public DeliveryStatus getStatus() { return status; }
    public String getNote() { return note; }
}
