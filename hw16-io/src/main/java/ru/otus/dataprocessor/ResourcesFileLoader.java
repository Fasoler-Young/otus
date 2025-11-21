package ru.otus.dataprocessor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.NoSuchFileException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.model.Measurement;

public class ResourcesFileLoader implements Loader {

    private static final Logger logger = LoggerFactory.getLogger(ResourcesFileLoader.class);
    private final ObjectMapper mapper;
    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.mapper = JsonMapper.builder().build();
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        try {

            InputStream systemResourceAsStream = ClassLoader.getSystemResourceAsStream(fileName);
            if (systemResourceAsStream == null) {
                throw new NoSuchFileException(fileName);
            }
            byte[] readAllBytes = systemResourceAsStream.readAllBytes();
            return mapper.readValue(readAllBytes, new TypeReference<>() {});
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new FileProcessException(e);
        }
    }
}
