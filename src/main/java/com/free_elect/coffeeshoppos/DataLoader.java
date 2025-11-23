package com.free_elect.coffeeshoppos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.Instant;

@Component
public class DataLoader implements CommandLineRunner {

    private final CustomerRepository customerRepo;
    private final TransactionRepository txRepo;

    public DataLoader(CustomerRepository customerRepo, TransactionRepository txRepo) {
        this.customerRepo = customerRepo;
        this.txRepo = txRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        // create a customer using convenience constructor
        Customer c = new Customer("Ana", "09171234567");
        c = customerRepo.save(c); // now c.getId() is available

        // create a transaction
        Transaction t = new Transaction();
        t.setCustomerId(c.getId()); // if you use customerId pattern
        t.setTotal(new BigDecimal("532.00"));
        t.setDiscount(new BigDecimal("26.60"));
        t.setFinalTotal(new BigDecimal("505.40"));
        t.setTransactionDate(Instant.now());

        // add items using convenience constructor
        TransactionItem item1 = new TransactionItem("Latte", 2, new BigDecimal("266.00"));
        item1.setCategory("Coffee");
        t.addItem(item1);

        txRepo.save(t);
    }
}