package com.businessmanagement.sales.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "sales_delivery")
public class SalesDelivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long deliveryId;

    @Column(name = "contract_id", nullable = false)
    private Long contractId;

    @Column(name = "weigh_ticket_id")
    private Long weighTicketId;

    @Column(name = "warehouse_id")
    private Long warehouseId;

    @Column(name = "vehicle_id")
    private Long vehicleId;

    @Column(name = "truck_license_plate", length = 30)
    private String truckLicensePlate;

    @Column(name = "delivered_actual_kg", nullable = false, precision = 14, scale = 3)
    private BigDecimal deliveredActualKg;

    @Column(name = "delivered_standard_kg", nullable = false, precision = 14, scale = 3)
    private BigDecimal deliveredStandardKg;

    @Column(name = "customer_standard_kg", precision = 14, scale = 3)
    private BigDecimal customerStandardKg;

    @Column(name = "discrepancy_kg", precision = 14, scale = 3)
    private BigDecimal discrepancyKg;

    @Column(name = "excess_handling", length = 30)
    private String excessHandling;

    @Column(name = "delivery_date", nullable = false)
    private LocalDate deliveryDate;

    @Column(name = "note", length = 1000)
    private String note;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "CONFIRMED";

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() { createdAt = LocalDateTime.now(); }

    public Long getDeliveryId() { return deliveryId; }
    public void setDeliveryId(Long deliveryId) { this.deliveryId = deliveryId; }
    public Long getContractId() { return contractId; }
    public void setContractId(Long contractId) { this.contractId = contractId; }
    public Long getWeighTicketId() { return weighTicketId; }
    public void setWeighTicketId(Long weighTicketId) { this.weighTicketId = weighTicketId; }
    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
    public String getTruckLicensePlate() { return truckLicensePlate; }
    public void setTruckLicensePlate(String truckLicensePlate) { this.truckLicensePlate = truckLicensePlate; }
    public BigDecimal getDeliveredActualKg() { return deliveredActualKg; }
    public void setDeliveredActualKg(BigDecimal deliveredActualKg) { this.deliveredActualKg = deliveredActualKg; }
    public BigDecimal getDeliveredStandardKg() { return deliveredStandardKg; }
    public void setDeliveredStandardKg(BigDecimal deliveredStandardKg) { this.deliveredStandardKg = deliveredStandardKg; }
    public BigDecimal getCustomerStandardKg() { return customerStandardKg; }
    public void setCustomerStandardKg(BigDecimal customerStandardKg) { this.customerStandardKg = customerStandardKg; }
    public BigDecimal getDiscrepancyKg() { return discrepancyKg; }
    public void setDiscrepancyKg(BigDecimal discrepancyKg) { this.discrepancyKg = discrepancyKg; }
    public String getExcessHandling() { return excessHandling; }
    public void setExcessHandling(String excessHandling) { this.excessHandling = excessHandling; }
    public LocalDate getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(LocalDate deliveryDate) { this.deliveryDate = deliveryDate; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}