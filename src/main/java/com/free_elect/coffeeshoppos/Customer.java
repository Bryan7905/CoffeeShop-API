package com.free_elect.coffeeshoppos;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // could be email or phone
    private String contact;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Transaction> transactions = new ArrayList<>();

    public Customer() {}

    public Customer(String name, String contact) {
        this.name = name;
        this.contact = contact;
    }

    // getters / setters

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
        t.setCustomer(this);
    }

    public void removeTransaction(Transaction t) {
        transactions.remove(t);
        t.setCustomer(null);
    }

    @Transient
    public int getTransactionsCount() {
        return transactions.size();
    }
}