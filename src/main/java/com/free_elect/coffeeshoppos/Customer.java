package com.free_elect.coffeeshoppos;

import jakarta.persistence.*;

@Entity
@Table(name = "customers")
public class Customer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String contact;
    private Integer transactions = 0;

    // JPA requires a no-arg constructor
    public Customer() { }

    // Convenience constructor used by DataLoader
    public Customer(String name, String contact) {
        this.name = name;
        this.contact = contact;
        this.transactions = 0;
    }

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public Integer getTransactions() { return transactions; }
    public void setTransactions(Integer transactions) { this.transactions = transactions; }
}