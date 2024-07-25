package com.example.consumerservice.model;

import com.example.consumerservice.convert.StringToJsonSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "metrics")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Metric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    @JsonProperty(value = "name", required = true)
    private String name;

    @Column(name = "data", nullable = false, columnDefinition = "text")
    @JsonProperty(value = "data", required = true)
    @JsonSerialize(using = StringToJsonSerializer.class)
    private String data;

    @Column(name = "description")
    @JsonProperty(value = "description")
    private String description;
}
