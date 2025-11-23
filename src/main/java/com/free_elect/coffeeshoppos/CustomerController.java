package com.free_elect.coffeeshoppos;

import com.free_elect.coffeeshoppos.Customer;
import com.free_elect.coffeeshoppos.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
        import java.util.List;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "https://coffeeshop-ui.onrender.com")
public class CustomerController {

    private final CustomerRepository repo;
    public CustomerController(CustomerRepository repo){ this.repo = repo; }

    @GetMapping
    public List<Customer> list(){ return repo.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> get(@PathVariable Long id){
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody Customer payload){
        // allow client to provide id (optional)
        if (payload.getId() != null && repo.existsById(payload.getId())) {
            return ResponseEntity.status(409).build();
        }
        Customer saved = repo.save(payload);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable Long id, @RequestBody Customer payload){
        return repo.findById(id).map(existing -> {
            existing.setName(payload.getName());
            existing.setContact(payload.getContact());
            existing.setTransactions(payload.getTransactions() == null ? existing.getTransactions() : payload.getTransactions());
            return ResponseEntity.ok(repo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}