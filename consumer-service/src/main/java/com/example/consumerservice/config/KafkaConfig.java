package com.example.consumerservice.config;

import com.example.consumerservice.model.Metric;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.JacksonUtils;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

/**
 * Конфигурация Kafka для потребления сообщений типа {@link Metric}.
 * <p>
 * Этот класс настраивает компоненты Kafka, такие как {@link ConsumerFactory},
 * {@link KafkaListenerContainerFactory} и {@link ObjectMapper}, необходимые для
 * десериализации сообщений из Kafka и их обработки.
 * </p>
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaConfig {
    @Bean
    public ObjectMapper objectMapper() {
        return JacksonUtils.enhancedObjectMapper();
    }

    /**
     * Создает {@link ConsumerFactory} для потребления сообщений типа {@link Metric}.
     *
     * @param kafkaProperties Свойства Kafka, используемые для конфигурации потребителя.
     * @param mapper          {@link ObjectMapper} для десериализации сообщений.
     * @return Экземпляр {@link ConsumerFactory}.
     */
    @Bean
    public ConsumerFactory<String, Metric> consumerFactory(KafkaProperties kafkaProperties, ObjectMapper mapper) {
        Map<String, Object> properties = kafkaProperties.buildConsumerProperties(null);

        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        properties.put(JsonDeserializer.TYPE_MAPPINGS,
                "com.example.producerservice.model.Metric:com.example.consumerservice.model.Metric");
        properties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);
        properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 10_000);

        DefaultKafkaConsumerFactory<String, Metric> kafkaConsumerFactory = new DefaultKafkaConsumerFactory<>(properties);
        kafkaConsumerFactory.setValueDeserializer(new JsonDeserializer<>(mapper));

        return kafkaConsumerFactory;
    }

    /**
     * Создает {@link KafkaListenerContainerFactory} для обработки сообщений типа {@link Metric}.
     * <p>
     * Настраивает контейнер для прослушивания сообщений в пакетном режиме с использованием
     * {@link SimpleAsyncTaskExecutor} для выполнения задач.
     * </p>
     *
     * @param consumerFactory {@link ConsumerFactory} для создания потребителей Kafka.
     * @return Экземпляр {@link KafkaListenerContainerFactory}.
     */
    @Bean("listenerContainerFactory")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Metric>>
    listenerContainerFactory(ConsumerFactory<String, Metric> consumerFactory) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, Metric>();
        factory.setConsumerFactory(consumerFactory);
        factory.setBatchListener(true);
        factory.setConcurrency(1);

        SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor("k-consumer-");
        factory.getContainerProperties().setListenerTaskExecutor(simpleAsyncTaskExecutor);

        return factory;
    }
}
