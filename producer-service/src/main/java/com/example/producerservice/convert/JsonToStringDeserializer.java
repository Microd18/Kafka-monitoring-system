package com.example.producerservice.convert;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Десериализатор для преобразования JSON в строку.
 * <p>
 * Этот класс используется для обработки JSON, который может быть представлен как строка или как структура JSON.
 * Он конвертирует JSON-значения в строковое представление.
 * </p>
 */
@Component
public class JsonToStringDeserializer extends JsonDeserializer<String> {

    /**
     * Десериализует JSON-данные в строку.
     * <p>
     * Если JSON-данные представлены как строка, возвращает текстовое значение.
     * В противном случае, преобразует JSON-структуру в строку с помощью {@link JsonParser#readValueAsTree()} и возвращает это значение.
     * </p>
     *
     * @param parser   {@link JsonParser} для чтения JSON данных
     * @param context  {@link DeserializationContext} для десериализации
     * @return Строковое представление JSON-данных
     * @throws IOException Если произошла ошибка ввода-вывода при чтении JSON-данных
     */
    @Override
    public String deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        if (parser.hasToken(JsonToken.VALUE_STRING)) {
            return parser.getText();
        }

        return parser.readValueAsTree().toString();
    }
}
