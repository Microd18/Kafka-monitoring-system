package com.example.producerservice.model;

import com.example.producerservice.convert.JsonToStringDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Представляет модель метрики в системе.
 * <p>
 * Этот класс используется для передачи данных метрик, собранных из различных источников, и включает в себя
 * обязательные поля для имени и данных метрики, а также необязательное поле для описания метрики.
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Metric {

    @JsonProperty(value = "name", required = true)
    @NotEmpty
    private String name;

    @JsonProperty(value = "data", required = true)
    @JsonDeserialize(using = JsonToStringDeserializer.class)
    @NotBlank
    private String data;

    @JsonProperty(value = "description")
    private String description;
}
