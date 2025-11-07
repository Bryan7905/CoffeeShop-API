package com.free_elect.coffeeshoppos;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionRepository txnRepo;
    private final CustomerRepository customerRepo;

    public TransactionController(TransactionRepository txnRepo, CustomerRepository customerRepo) {
        this.txnRepo = txnRepo;
        this.customerRepo = customerRepo;
    }

    @GetMapping
    public java.util.List<Transaction> list() {
        return txnRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> get(@PathVariable Long id) {
        return txnRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody TransactionRequest request) {
        return customerRepo.findById(request.getCustomerId()).map(customer -> {
            Transaction txn = new Transaction();
            txn.setCustomer(customer);
            // build items
            txn.setItems(request.getItems().stream()
                    .map(i -> new TransactionItem(i.getName(), i.getQty(), i.getPrice()))
                    .collect(Collectors.toList()));
            // compute totals
            BigDecimal total = txn.getItems().stream()
                    .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQty())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            txn.setTotal(total);
            BigDecimal discount = request.getDiscount() != null ? request.getDiscount() : BigDecimal.ZERO;
            txn.setDiscount(discount);
            txn.setFinalTotal(total.subtract(discount));
            Transaction saved = txnRepo.save(txn);
            // maintain bidirectional association
            customer.getTransactions().add(saved);
            customerRepo.save(customer);
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.badRequest().build());
    }
}