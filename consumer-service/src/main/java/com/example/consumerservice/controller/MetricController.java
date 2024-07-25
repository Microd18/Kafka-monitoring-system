package com.example.consumerservice.controller;

import com.example.consumerservice.exceptions.MetricNotFoundException;
import com.example.consumerservice.model.Metric;
import com.example.consumerservice.service.MetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/metrics")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MetricController {

    private final MetricService service;

    @GetMapping
    public ResponseEntity<?> getAllMetrics() {
        List<Metric> metrics = service.findAll();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(metrics);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMetric(@PathVariable Integer id) {

        Metric metric = service.findById(id);
        if (metric == null) {
            throw new MetricNotFoundException();
        }
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(metric);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveMetric(@RequestBody Metric metric) {

        if (metric == null) {
            throw new MetricNotFoundException();
        }
        service.saveMetric(metric);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(metric);
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<?> deleteAllMetrics() {

        service.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
