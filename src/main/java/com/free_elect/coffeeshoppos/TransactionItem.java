package com.free_elect.coffeeshoppos;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Embeddable
public class TransactionItem {

    private String name;

    private int qty;

    private BigDecimal price;

    public TransactionItem() {}

    public TransactionItem(String name, int qty, BigDecimal price) {
        this.name = name;
        this.qty = qty;
        this.price = price;
    }

    // getters / setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
