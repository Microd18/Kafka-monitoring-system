package com.example.consumerservice.service.impl;

import com.example.consumerservice.exceptions.MetricNotFoundException;
import com.example.consumerservice.model.Metric;
import com.example.consumerservice.repository.MetricRepository;
import com.example.consumerservice.service.MetricService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Реализация сервиса для работы с метриками.
 * <p>
 * Этот класс реализует интерфейс {@link MetricService} и предоставляет методы для сохранения, поиска и удаления метрик.
 * </p>
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MetricServiceImpl implements MetricService {

    private final MetricRepository metricRepository;

    @Transactional
    @Override
    public Metric saveMetric(Metric metric) {
        return this.metricRepository.save(metric);
    }

    @Override
    public List<Metric> findAll() {
        return this.metricRepository.findAll();
    }

    @Override
    public Metric findById(Integer id) {
        return metricRepository.findById(id).orElseThrow(MetricNotFoundException::new);
    }

    @Transactional
    @Override
    public void deleteAll() {
        metricRepository.deleteAll();
    }
}
