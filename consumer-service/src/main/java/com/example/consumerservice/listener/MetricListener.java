package com.example.consumerservice.listener;

import com.example.consumerservice.listener.consumer.MetricMessagesConsumer;
import com.example.consumerservice.model.Metric;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class MetricListener {

    private final MetricMessagesConsumer metricMessagesConsumer;
    @KafkaListener(
            topics = "${consumer-service.kafka}",
            containerFactory = "listenerContainerFactory")
    public void listen(@Payload List<Message<Metric>> values) {
        this.metricMessagesConsumer.accept(values);
    }
}
