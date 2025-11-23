package com.free_elect.coffeeshoppos;

import com.free_elect.coffeeshoppos.Transaction;
import com.free_elect.coffeeshoppos.Customer;
import com.free_elect.coffeeshoppos.TransactionRepository;
import com.free_elect.coffeeshoppos.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "https://coffeeshop-ui.onrender.com")
public class TransactionController {

    private final TransactionRepository txRepo;
    private final CustomerRepository customerRepo;

    public TransactionController(TransactionRepository txRepo, CustomerRepository customerRepo){
        this.txRepo = txRepo;
        this.customerRepo = customerRepo;
    }

    @GetMapping
    public List<Transaction> list(){ return txRepo.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> get(@PathVariable Long id){
        return txRepo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Transaction> create(@RequestBody Transaction payload){
        // ensure items have transaction set
        if (payload.getTransactionDate() == null) payload.setTransactionDate(Instant.now());
        if (payload.getItems()!=null){
            payload.getItems().forEach(i -> i.setTransaction(payload));
        }
        Transaction saved = txRepo.save(payload);

        // update customer's transactions count (best-effort)
        if (saved.getCustomerId()!=null){
            customerRepo.findById(saved.getCustomerId()).ifPresent(c -> {
                c.setTransactions((c.getTransactions()==null?0:c.getTransactions()) + 1);
                customerRepo.save(c);
            });
        }

        return ResponseEntity.ok(saved);
    }
}