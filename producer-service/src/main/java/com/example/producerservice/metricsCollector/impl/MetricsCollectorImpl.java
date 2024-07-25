package com.example.producerservice.metricsCollector.impl;

import com.example.producerservice.metricsCollector.MetricsCollector;
import com.example.producerservice.model.Metric;
import com.example.producerservice.service.impl.MetricsSenderImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class MetricsCollectorImpl implements MetricsCollector {

    private final MetricsEndpoint metricsEndpoint;
    private final HealthEndpoint healthEndpoint;
    private final ObjectMapper objectMapper;
    private final MetricsSenderImpl metricsSenderImpl;

    @Override
    public void sendAllMetrics() {
        try {
            for (Metric metrics : this.collectAllMetrics()) {
                this.metricsSenderImpl.send(metrics);
            }
        } catch (Exception e) {
            log.error("Error while publishing metrics data", e);
            throw new RuntimeException(e);
        }
    }

    public List<Metric> collectAllMetrics() {
        try {
            return Stream.of(collectHealthMetrics(), collectCommonMetrics())
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
        } catch (JsonProcessingException e) {
            log.error("Error while collecting metrics data", e);
            throw new RuntimeException(e);
        }
    }

    public List<Metric> collectHealthMetrics() throws JsonProcessingException {
        return Collections.singletonList(Metric.builder()
                .name("health")
                .data(objectMapper.writeValueAsString(healthEndpoint.health()))
                .description("Detailed information about the health of the application")
                .build());
    }

    public List<Metric> collectCommonMetrics() throws JsonProcessingException {
        List<Metric> resultList = new ArrayList<>();
        for (String descriptorName : metricsEndpoint.listNames().getNames()) {
            MetricsEndpoint.MetricDescriptor metricDescriptor = metricsEndpoint.metric(descriptorName, null);
            Metric metric = Metric.builder()
                    .name(descriptorName)
                    .data(objectMapper.writeValueAsString(metricDescriptor))
                    .description(Optional.ofNullable(metricDescriptor.getDescription()).orElse(""))
                    .build();
            resultList.add(metric);
        }
        return resultList;
    }
}
