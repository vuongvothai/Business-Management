package com.businessmanagement.weighticket.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "weigh_ticket_line")
public class WeighTicketLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weigh_line_id")
    private Long weighLineId;

    @Column(name = "weigh_ticket_id", nullable = false)
    private Long weighTicketId;

    @Column(name = "line_no", nullable = false)
    private int lineNo;

    @Column(name = "weight_kg", nullable = false, precision = 12, scale = 3)
    private BigDecimal weightKg;

    @Column(name = "bag_count", nullable = false)
    private int bagCount;

    @Column(name = "bag_type", length = 30)
    private String bagType;

    @Column(name = "tare_per_bag_kg", precision = 8, scale = 3)
    private BigDecimal tarePerBagKg;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() { createdAt = LocalDateTime.now(); }

    public Long getWeighLineId() { return weighLineId; }
    public void setWeighLineId(Long weighLineId) { this.weighLineId = weighLineId; }
    public Long getWeighTicketId() { return weighTicketId; }
    public void setWeighTicketId(Long weighTicketId) { this.weighTicketId = weighTicketId; }
    public int getLineNo() { return lineNo; }
    public void setLineNo(int lineNo) { this.lineNo = lineNo; }
    public BigDecimal getWeightKg() { return weightKg; }
    public void setWeightKg(BigDecimal weightKg) { this.weightKg = weightKg; }
    public int getBagCount() { return bagCount; }
    public void setBagCount(int bagCount) { this.bagCount = bagCount; }
    public String getBagType() { return bagType; }
    public void setBagType(String bagType) { this.bagType = bagType; }
    public BigDecimal getTarePerBagKg() { return tarePerBagKg; }
    public void setTarePerBagKg(BigDecimal tarePerBagKg) { this.tarePerBagKg = tarePerBagKg; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}