package com.example.consumerservice.listener.consumer;

import com.example.consumerservice.model.Metric;
import org.springframework.messaging.Message;

import java.util.List;
import java.util.function.Consumer;

public interface MetricMessagesConsumer extends Consumer<List<Message<Metric>>> {
}
