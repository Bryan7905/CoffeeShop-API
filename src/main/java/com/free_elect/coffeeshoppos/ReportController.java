package com.free_elect.coffeeshoppos;

import com.free_elect.coffeeshoppos.Transaction;
import com.free_elect.coffeeshoppos.TransactionRepository;
import com.free_elect.coffeeshoppos.CustomerRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "https://coffeeshop-ui.onrender.com")
public class ReportController {

    private final TransactionRepository txRepo;
    private final CustomerRepository customerRepo;
    public ReportController(TransactionRepository txRepo, CustomerRepository customerRepo){
        this.txRepo = txRepo;
        this.customerRepo = customerRepo;
    }

    @GetMapping("/reports")
    public ResponseEntity<?> getReports(
            @RequestParam(required = false) String frame,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant end
    ){
        // derive start/end from frame if provided
        if (frame != null && (start==null || end==null)) {
            // support basic frames: week (7 days incl today), 1month(30), 3months(90), 6months, 1year
            int days = switch (frame) {
                case "week" -> 7;
                case "1month" -> 30;
                case "3months" -> 90;
                case "6months" -> 180;
                case "1year" -> 365;
                default -> 7;
            };
            // cutoff includes today (start at start-of-day)
            LocalDate today = LocalDate.now(ZoneOffset.UTC);
            LocalDate startDate = today.minusDays(days - 1);
            start = startDate.atStartOfDay().toInstant(ZoneOffset.UTC);
            end = today.atTime(23,59,59).toInstant(ZoneOffset.UTC);
        }

        if (start == null) {
            // default to last 7 days
            start = Instant.now().minusSeconds(7L * 86400L);
        }
        if (end == null) end = Instant.now();

        List<Transaction> txns = txRepo.findByTransactionDateBetween(start, end);

        // total revenue & discounts & customers set
        double totalRevenue = txns.stream().mapToDouble(t -> t.getFinalTotal()==null?0: t.getFinalTotal().doubleValue()).sum();
        double totalDiscount = txns.stream().mapToDouble(t -> t.getDiscount()==null?0: t.getDiscount().doubleValue()).sum();
        int totalTxns = txns.size();

        // compute item sales map
        Map<String, Map<String, Object>> itemMap = new HashMap<>();
        for (Transaction t : txns) {
            if (t.getItems() == null) continue;
            t.getItems().forEach(it -> {
                String name = it.getName();
                Map<String, Object> entry = itemMap.computeIfAbsent(name, k -> {
                    Map<String,Object> m = new HashMap<>();
                    m.put("name", name);
                    m.put("qty", 0);
                    m.put("category", it.getCategory());
                    return m;
                });
                int old = (int)entry.get("qty");
                entry.put("qty", old + (it.getQty()==null?0:it.getQty()));
            });
        }

        // sort items
        List<Map<String,Object>> itemList = itemMap.values().stream()
                .sorted((a,b) -> Integer.compare((int)b.get("qty"), (int)a.get("qty")))
                .collect(Collectors.toList());

        Map<String,Object> topDrink = itemList.stream().filter(i -> {
            String cat = (String)i.get("category");
            return "Coffee".equalsIgnoreCase(cat) || "Drinks".equalsIgnoreCase(cat);
        }).findFirst().orElse(null);

        Map<String,Object> topPastry = itemList.stream().filter(i -> {
            String cat = (String)i.get("category");
            return "Pastry".equalsIgnoreCase(cat);
        }).findFirst().orElse(null);

        // customers in frame
        Map<Long, Long> customerTxnCount = txns.stream().filter(t -> t.getCustomerId()!=null)
                .collect(Collectors.groupingBy(Transaction::getCustomerId, Collectors.counting()));

        List<Map<String,Object>> customersInFrame = customerTxnCount.entrySet().stream()
                .map(e -> {
                    Map<String,Object> m = new HashMap<>();
                    m.put("id", e.getKey());
                    m.put("txnCount", e.getValue());
                    customerRepo.findById(e.getKey()).ifPresent(c -> m.put("name", c.getName()));
                    if (!m.containsKey("name")) m.put("name", "Customer " + e.getKey());
                    return m;
                })
                .sorted((a,b) -> Long.compare((Long)b.get("txnCount"), (Long)a.get("txnCount")))
                .collect(Collectors.toList());

        long customersCount = customerTxnCount.size();

        Map<String,Object> salesSummary = new HashMap<>();
        salesSummary.put("totalTransactions", totalTxns);
        salesSummary.put("totalRevenue", totalRevenue);
        salesSummary.put("totalDiscount", totalDiscount);
        salesSummary.put("averageOrderValue", totalTxns>0? totalRevenue/totalTxns : 0);

        Map<String,Object> response = new HashMap<>();
        response.put("frameId", frame == null ? "custom" : frame);
        response.put("frameLabel", frame == null ? "Custom Range" : frame);
        response.put("mostLoyalCustomer", customersInFrame.isEmpty()? null : customersInFrame.get(0));
        response.put("topDrink", topDrink);
        response.put("topPastry", topPastry);
        response.put("customersCount", customersCount);
        response.put("customersInFrame", customersInFrame);
        response.put("salesSummary", salesSummary);

        return ResponseEntity.ok(response);
    }
}
