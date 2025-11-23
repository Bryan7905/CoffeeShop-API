package com.free_elect.coffeeshoppos;

import com.free_elect.coffeeshoppos.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByTransactionDateBetween(Instant start, Instant end);
    List<Transaction> findByTransactionDateAfter(Instant start);
}
