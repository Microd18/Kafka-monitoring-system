package com.example.producerservice.controller;

import com.example.producerservice.metricsCollector.impl.MetricsCollectorImpl;
import com.example.producerservice.model.Metric;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/metrics")
@RequiredArgsConstructor
@Slf4j
public class MetricsController {

    private final MetricsCollectorImpl metricsCollector;

    @PostMapping
    public ResponseEntity<List<Metric>> publishMetrics() {
        List<Metric> metrics = metricsCollector.collectAllMetrics();
        metricsCollector.sendAllMetrics();
        return ResponseEntity.ok(metrics);
    }
}
