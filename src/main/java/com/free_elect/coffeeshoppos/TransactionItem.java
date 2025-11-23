package com.free_elect.coffeeshoppos;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "transaction_items")
public class TransactionItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer qty;
    private BigDecimal pricePhp;
    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    public TransactionItem() { }

    // Convenience constructor used by DataLoader: name, qty, pricePhp
    public TransactionItem(String name, int qty, BigDecimal pricePhp) {
        this.name = name;
        this.qty = qty;
        this.pricePhp = pricePhp;
    }

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getQty() { return qty; }
    public void setQty(Integer qty) { this.qty = qty; }

    public BigDecimal getPricePhp() { return pricePhp; }
    public void setPricePhp(BigDecimal pricePhp) { this.pricePhp = pricePhp; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Transaction getTransaction() { return transaction; }
    public void setTransaction(Transaction transaction) { this.transaction = transaction; }
}