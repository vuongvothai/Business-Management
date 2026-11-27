package com.businessmanagement.payment.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "payment_allocation")
public class PaymentAllocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "allocation_id")
    private Long allocationId;

    @Column(name = "voucher_id", nullable = false)
    private Long voucherId;

    @Column(name = "ref_type", nullable = false, length = 30)
    private String refType;

    @Column(name = "ref_id", nullable = false)
    private Long refId;

    @Column(name = "allocated_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal allocatedAmount;

    public Long getAllocationId() { return allocationId; }
    public void setAllocationId(Long allocationId) { this.allocationId = allocationId; }
    public Long getVoucherId() { return voucherId; }
    public void setVoucherId(Long voucherId) { this.voucherId = voucherId; }
    public String getRefType() { return refType; }
    public void setRefType(String refType) { this.refType = refType; }
    public Long getRefId() { return refId; }
    public void setRefId(Long refId) { this.refId = refId; }
    public BigDecimal getAllocatedAmount() { return allocatedAmount; }
    public void setAllocatedAmount(BigDecimal allocatedAmount) { this.allocatedAmount = allocatedAmount; }
}