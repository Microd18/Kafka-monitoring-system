package com.example.producerservice.metricsCollector.impl;

import com.example.producerservice.metricsCollector.MetricsCollector;
import com.example.producerservice.model.Metric;
import com.example.producerservice.service.impl.MetricsSenderImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Реализация {@link MetricsCollector}, отвечающая за сбор и отправку метрик.
 * <p>
 * Этот класс собирает метрики с помощью {@link MetricsEndpoint} и {@link HealthEndpoint},
 * преобразует их в объекты {@link Metric} и отправляет с помощью {@link MetricsSenderImpl}.
 * </p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MetricsCollectorImpl implements MetricsCollector {

    private final MetricsEndpoint metricsEndpoint;
    private final HealthEndpoint healthEndpoint;
    private final ObjectMapper objectMapper;
    private final MetricsSenderImpl metricsSenderImpl;

    /**
     * Отправляет все собранные метрики.
     * <p>
     * Сначала собирает метрики с помощью {@link #collectAllMetrics()} и затем отправляет их
     * через {@link MetricsSenderImpl}. В случае ошибки выбрасывает {@link RuntimeException}.
     * </p>
     */
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

    /**
     * Собирает все метрики.
     * <p>
     * Объединяет метрики из методов {@link #collectHealthMetrics()} и {@link #collectCommonMetrics()},
     * а затем возвращает их в виде списка. В случае ошибки при сборе метрик выбрасывает {@link RuntimeException}.
     * </p>
     *
     * @return Список собранных метрик
     * @throws JsonProcessingException Если произошла ошибка при обработке JSON
     */
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

    /**
     * Собирает метрики приложения.
     * <p>
     * Возвращает список, содержащий одну метрику, представляющую состояние приложения.
     * </p>
     *
     * @return Список метрик
     * @throws JsonProcessingException Если произошла ошибка при обработке JSON
     */
    public List<Metric> collectHealthMetrics() throws JsonProcessingException {
        return Collections.singletonList(Metric.builder()
                .name("health")
                .data(objectMapper.writeValueAsString(healthEndpoint.health()))
                .description("Detailed information about the health of the application")
                .build());
    }

    /**
     * Собирает общие метрики приложения.
     * <p>
     * Перебирает все метрики, предоставляемые {@link MetricsEndpoint}, и возвращает их в виде списка.
     * </p>
     *
     * @return Список общих метрик
     * @throws JsonProcessingException Если произошла ошибка при обработке JSON
     */
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
