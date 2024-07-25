package com.example.consumerservice.listener.consumer.impl;

import com.example.consumerservice.listener.consumer.MetricMessagesConsumer;
import com.example.consumerservice.model.Metric;
import com.example.consumerservice.service.MetricService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetricMessagesConsumerImpl implements MetricMessagesConsumer {

    private final MetricService metricService;
    @Override
    public void accept(List<Message<Metric>> messages) {
        messages.forEach(message -> {
            Metric payload = message.getPayload();
            Metric save = this.metricService.saveMetric(payload);
            log.info("Metrics '{}' has been persisted", save.getName());
        });
    }

}
