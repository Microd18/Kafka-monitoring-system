package com.example.consumerservice.convert;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Сериализатор для преобразования строки в JSON.
 * <p>
 * Этот сериализатор используется для преобразования строки, которая представляет собой JSON,
 * в объект JSON при сериализации.
 * </p>
 */
@Component
public class StringToJsonSerializer extends JsonSerializer<String> {

    /**
     * Сериализует строку в JSON.
     *
     * @param s                Строка для сериализации.
     * @param jsonGenerator    {@link JsonGenerator} для записи JSON.
     * @param serializerProvider {@link SerializerProvider} для предоставления дополнительных настроек сериализации.
     * @throws IOException Если возникает ошибка ввода-вывода во время сериализации.
     */
    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        try (JsonParser parser = jsonGenerator.getCodec().getFactory().createParser(s)) {
            TreeNode tree = parser.readValueAsTree();
            jsonGenerator.writeTree(tree);
        } catch (JsonProcessingException e) {
            jsonGenerator.writeString(s);
        }
    }
}
