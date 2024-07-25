package com.example.consumerservice.exceptions.handler;

import com.example.consumerservice.exceptions.MetricNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerClass {
    @ExceptionHandler(MetricNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String metricNotFoundExceptionHandler(MetricNotFoundException e) {
        return "Metric not found!";
    }
}
