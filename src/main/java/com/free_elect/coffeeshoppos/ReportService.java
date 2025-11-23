package com.free_elect.coffeeshoppos;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    private final ReportRepository repository;

    public ReportService(ReportRepository repository) {
        this.repository = repository;
    }

    public List<Report> findAll() {
        return repository.findAll();
    }

    public Optional<Report> findById(Long id) {
        return repository.findById(id);
    }

    public Report save(Report report) {
        return repository.save(report);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
