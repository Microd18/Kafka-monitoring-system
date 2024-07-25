package com.example.producerservice.controller;

import com.example.producerservice.metricsCollector.impl.MetricsCollectorImpl;
import com.example.producerservice.model.Metric;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/send")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class MetricsController {

    private final MetricsCollectorImpl metricsCollector;

    /**
     * Обрабатывает POST-запросы для отправки метрик.
     * <p>
     * Собирает все метрики с помощью {@link MetricsCollectorImpl#collectAllMetrics()}
     * и отправляет их с помощью {@link MetricsCollectorImpl#sendAllMetrics()}.
     * Возвращает список метрик в ответе.
     * </p>
     *
     * @return {@link ResponseEntity} содержащий список метрик и статус ответа
     */
    @PostMapping
    public ResponseEntity<List<Metric>> sendMetrics() {
        List<Metric> metrics = metricsCollector.collectAllMetrics();
        metricsCollector.sendAllMetrics();
        return ResponseEntity.ok(metrics);
    }
}
