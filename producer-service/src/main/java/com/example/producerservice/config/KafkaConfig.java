package com.example.producerservice.config;

import com.example.producerservice.model.Metric;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.JacksonUtils;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

/**
 * Конфигурация для настройки Kafka.
 * <p>
 * Этот класс содержит бины, которые необходимы для создания и настройки Kafka продюсера,
 * а также для создания топика Kafka.
 * </p>
 */
@Configuration
public class KafkaConfig {

    @Value("${kafka.topic.metrics}")
    private String topic;

    @Bean
    public ObjectMapper objectMapper() {
        return JacksonUtils.enhancedObjectMapper();
    }

    /**
     * Создает и возвращает {@link ProducerFactory} для создания Kafka продюсеров.
     * <p>
     * Настраивает сериализацию ключей и значений сообщений для продюсера. Ключи сериализуются
     * как строки, а значения сериализуются в формате JSON.
     * </p>
     *
     * @param kafkaProperties свойства Kafka, загруженные из конфигурации приложения
     * @param mapper          {@link ObjectMapper} для сериализации значений
     * @return {@link ProducerFactory} экземпляр
     */
    @Bean
    public ProducerFactory<String, Metric> producerFactory(KafkaProperties kafkaProperties, ObjectMapper mapper) {
        Map<String, Object> properties = kafkaProperties.buildProducerProperties(null);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        DefaultKafkaProducerFactory<String, Metric> factory = new DefaultKafkaProducerFactory<>(properties);
        factory.setValueSerializer(new JsonSerializer<>(mapper));
        return factory;
    }

    @Bean
    public KafkaTemplate<String, Metric> kafkaTemplate(ProducerFactory<String, Metric> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    /**
     * Создает и возвращает {@link NewTopic} для создания топика Kafka.
     * <p>
     * Топик создается с одним разделом и одной репликой. Имя топика задается из конфигурации приложения.
     * </p>
     *
     * @return {@link NewTopic} экземпляр
     */
    @Bean
    public NewTopic topic() {
        return TopicBuilder.name(topic)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
