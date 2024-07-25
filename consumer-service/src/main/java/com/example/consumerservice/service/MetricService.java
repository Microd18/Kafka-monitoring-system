package com.example.consumerservice.service;

import com.example.consumerservice.model.Metric;

import java.util.List;

public interface MetricService {

    Metric saveMetric(Metric metric);

    List<Metric> findAll();

    Metric findById(Integer id);

    void deleteAll();

}
