package com.free_elect.coffeeshoppos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner loadInitialData(CustomerRepository customerRepo, TransactionRepository tnxRepo) {
        return args -> {
            if (customerRepo.count() == 0) {
                Customer c1 = new Customer("Alice Smith", "alice@example.com");
                Customer c2 = new Customer("Bob Johnson", "555-1234");
                Customer c3 = new Customer("Charlie Brown", "charlie@mail.com");

                customerRepo.saveAll(List.of(c1, c2, c3));

                // Example transaction: { id: 1001, customerId: 1, items: [{ name: 'Latte', qty: 1, price: 5 }], total: 5, discount: 0.5, finalTotal: 4.5 }
                Transaction t = new Transaction();
                t.setCustomer(c1);
                t.setItems(List.of(new TransactionItem("Latte", 1, BigDecimal.valueOf(5))));
                t.setTotal(BigDecimal.valueOf(5));
                t.setDiscount(BigDecimal.valueOf(0.5));
                t.setFinalTotal(BigDecimal.valueOf(4.5));
                tnxRepo.save(t);

                // maintain bidirectional list
                c1.getTransactions().add(t);
                customerRepo.save(c1);
            }
        };
    }
}
