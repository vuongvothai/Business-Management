package com.businessmanagement.weighticket.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "weigh_ticket")
public class WeighTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weigh_ticket_id")
    private Long weighTicketId;

    @Column(name = "code", nullable = false, length = 30)
    private String code;

    @Column(name = "direction", nullable = false, length = 10)
    private String direction;

    @Column(name = "weighed_at", nullable = false)
    private LocalDateTime weighedAt;

    @Column(name = "warehouse_id")
    private Long warehouseId;

    @Column(name = "vehicle_id")
    private Long vehicleId;

    @Column(name = "truck_license_plate", length = 30)
    private String truckLicensePlate;

    @Column(name = "total_bags", nullable = false)
    private int totalBags;

    @Column(name = "gross_weight_kg", nullable = false, precision = 14, scale = 3)
    private BigDecimal grossWeightKg = BigDecimal.ZERO;

    @Column(name = "tare_weight_kg", nullable = false, precision = 14, scale = 3)
    private BigDecimal tareWeightKg = BigDecimal.ZERO;

    @Column(name = "net_weight_kg", nullable = false, precision = 14, scale = 3)
    private BigDecimal netWeightKg = BigDecimal.ZERO;

    @Column(name = "moisture_pct", precision = 5, scale = 2)
    private BigDecimal moisturePct;

    @Column(name = "impurity_pct", precision = 5, scale = 2)
    private BigDecimal impurityPct;

    @Column(name = "moisture_std", nullable = false, precision = 5, scale = 2)
    private BigDecimal moistureStd = new BigDecimal("12.50");

    @Column(name = "impurity_std", nullable = false, precision = 5, scale = 2)
    private BigDecimal impurityStd = new BigDecimal("1.00");

    @Column(name = "standard_weight_kg", nullable = false, precision = 14, scale = 3)
    private BigDecimal standardWeightKg = BigDecimal.ZERO;

    @Column(name = "sample_test_result", length = 500)
    private String sampleTestResult;

    @Column(name = "photo_url", length = 1000)
    private String photoUrl;

    @Column(name = "note", length = 1000)
    private String note;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "DRAFT";

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void onUpdate() { updatedAt = LocalDateTime.now(); }

    public Long getWeighTicketId() { return weighTicketId; }
    public void setWeighTicketId(Long weighTicketId) { this.weighTicketId = weighTicketId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }
    public LocalDateTime getWeighedAt() { return weighedAt; }
    public void setWeighedAt(LocalDateTime weighedAt) { this.weighedAt = weighedAt; }
    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
    public String getTruckLicensePlate() { return truckLicensePlate; }
    public void setTruckLicensePlate(String truckLicensePlate) { this.truckLicensePlate = truckLicensePlate; }
    public int getTotalBags() { return totalBags; }
    public void setTotalBags(int totalBags) { this.totalBags = totalBags; }
    public BigDecimal getGrossWeightKg() { return grossWeightKg; }
    public void setGrossWeightKg(BigDecimal grossWeightKg) { this.grossWeightKg = grossWeightKg; }
    public BigDecimal getTareWeightKg() { return tareWeightKg; }
    public void setTareWeightKg(BigDecimal tareWeightKg) { this.tareWeightKg = tareWeightKg; }
    public BigDecimal getNetWeightKg() { return netWeightKg; }
    public void setNetWeightKg(BigDecimal netWeightKg) { this.netWeightKg = netWeightKg; }
    public BigDecimal getMoisturePct() { return moisturePct; }
    public void setMoisturePct(BigDecimal moisturePct) { this.moisturePct = moisturePct; }
    public BigDecimal getImpurityPct() { return impurityPct; }
    public void setImpurityPct(BigDecimal impurityPct) { this.impurityPct = impurityPct; }
    public BigDecimal getMoistureStd() { return moistureStd; }
    public void setMoistureStd(BigDecimal moistureStd) { this.moistureStd = moistureStd; }
    public BigDecimal getImpurityStd() { return impurityStd; }
    public void setImpurityStd(BigDecimal impurityStd) { this.impurityStd = impurityStd; }
    public BigDecimal getStandardWeightKg() { return standardWeightKg; }
    public void setStandardWeightKg(BigDecimal standardWeightKg) { this.standardWeightKg = standardWeightKg; }
    public String getSampleTestResult() { return sampleTestResult; }
    public void setSampleTestResult(String sampleTestResult) { this.sampleTestResult = sampleTestResult; }
    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}