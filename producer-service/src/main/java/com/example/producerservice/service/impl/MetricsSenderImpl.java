package com.example.producerservice.service.impl;

import com.example.producerservice.model.Metric;
import com.example.producerservice.service.MetricsSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class MetricsSenderImpl implements MetricsSender<String, Metric> {

    private final NewTopic topic;
    private final KafkaTemplate<String, Metric> kafkaTemplate;

    @Override
    public CompletableFuture<SendResult<String, Metric>> send(Metric value) {
        try {
            return this.kafkaTemplate.send(this.topic.name(), value.getName(), value)
                    .whenComplete((res, err) -> {
                        if (err == null) {
                            log.info("Metrics data: '{}' has been published", value.getName());
                        } else {
                            log.error("Metrics data: '{}' failed to get published", value.getName(), err);
                        }
                    });
        } catch (Exception e) {
            log.error("Error while trying to publish metrics data: {}", value, e);
            throw e;
        }
    }
}
