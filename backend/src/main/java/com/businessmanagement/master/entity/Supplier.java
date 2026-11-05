package com.businessmanagement.master.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "supplier")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "identity_no", length = 50)
    private String identityNo;

    @Column(nullable = false, length = 30)
    private String phone;

    @Column(length = 500)
    private String address;

    @Column(name = "usable_coffee_kg", nullable = false, precision = 14, scale = 3)
    private BigDecimal usableCoffeeKg = BigDecimal.ZERO;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected Supplier() {
    }

    public Supplier(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIdentityNo() {
        return identityNo;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public BigDecimal getUsableCoffeeKg() {
        return usableCoffeeKg;
    }
}
