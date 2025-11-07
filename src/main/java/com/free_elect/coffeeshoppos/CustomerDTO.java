package com.free_elect.coffeeshoppos;

public class CustomerDTO {
    private Long id;
    private String name;
    private String contact;
    private Integer transactions;

    public CustomerDTO() {}

    public CustomerDTO(Long id, String name, String contact, Integer transactions) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.transactions = transactions;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public Integer getTransactions() { return transactions; }
    public void setTransactions(Integer transactions) { this.transactions = transactions; }
}
