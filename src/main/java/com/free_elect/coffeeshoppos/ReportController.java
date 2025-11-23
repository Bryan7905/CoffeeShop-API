package com.free_elect.coffeeshoppos;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "${app.cors.allowed-origins:http://localhost:5173}")
public class ReportController {

    private final ReportService service;

    public ReportController(ReportService service) {
        this.service = service;
    }

    @GetMapping
    public List<Report> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Report> get(@PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Report> create(@RequestBody Report request) {
        Report created = service.save(request);
        return ResponseEntity.created(URI.create("/api/reports/" + created.getId())).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
