package com.example.consumerservice.repository;

import com.example.consumerservice.model.Metric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetricRepository extends JpaRepository<Metric, Integer> {
}
