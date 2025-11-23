package com.free_elect.coffeeshoppos;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;      // use this if you prefer id reference
    private BigDecimal total;
    private BigDecimal discount;
    private BigDecimal finalTotal;
    private Instant transactionDate;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionItem> items = new ArrayList<>();

    public Transaction() { }

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }

    public BigDecimal getFinalTotal() { return finalTotal; }
    public void setFinalTotal(BigDecimal finalTotal) { this.finalTotal = finalTotal; }

    public Instant getTransactionDate() { return transactionDate; }
    public void setTransactionDate(Instant transactionDate) { this.transactionDate = transactionDate; }

    public List<TransactionItem> getItems() { return items; }
    public void setItems(List<TransactionItem> items) {
        this.items.clear();
        if (items != null) {
            items.forEach(i -> {
                i.setTransaction(this);
                this.items.add(i);
            });
        }
    }

    public void addItem(TransactionItem item) {
        item.setTransaction(this);
        this.items.add(item);
    }
}