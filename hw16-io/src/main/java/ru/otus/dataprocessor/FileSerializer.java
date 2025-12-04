package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileSerializer implements Serializer {
    private static final Logger logger = LoggerFactory.getLogger(FileSerializer.class);
    private final ObjectMapper mapper;
    private final String fullOutputFilePath;

    public FileSerializer(String fullOutputFilePath) {
        this.mapper = JsonMapper.builder().build();
        this.fullOutputFilePath = fullOutputFilePath;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        // формирует результирующий json и сохраняет его в файл
        try {
            File file = new File(fullOutputFilePath);
            mapper.writeValue(file, data);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new FileProcessException(e);
        }
    }
}
