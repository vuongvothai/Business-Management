package com.businessmanagement.market.entity;

import com.businessmanagement.master.entity.Item;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "market_price")
public class MarketPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "market_price_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "price_date", nullable = false)
    private LocalDate priceDate;

    @Column(name = "price_per_kg", nullable = false, precision = 15, scale = 2)
    private BigDecimal pricePerKg;

    @Column(length = 255)
    private String source;

    @Column(name = "fetched_at")
    private LocalDateTime fetchedAt;

    protected MarketPrice() {
    }

    public MarketPrice(Item item, LocalDate priceDate, BigDecimal pricePerKg,
                       String source, LocalDateTime fetchedAt) {
        this.item = item;
        this.priceDate = priceDate;
        this.pricePerKg = pricePerKg;
        this.source = source;
        this.fetchedAt = fetchedAt;
    }

    public Long getId() { return id; }
    public Item getItem() { return item; }
    public LocalDate getPriceDate() { return priceDate; }
    public BigDecimal getPricePerKg() { return pricePerKg; }
    public String getSource() { return source; }
    public LocalDateTime getFetchedAt() { return fetchedAt; }
}
