package ru.otus.dataprocessor;

import java.util.*;
import java.util.stream.Collectors;
import ru.otus.model.Measurement;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        if (data == null || data.isEmpty()) {
            return Collections.emptyMap();
        }
        // группирует выходящий список по name, при этом суммирует поля value
        return data.stream()
                .collect(Collectors.toMap(Measurement::name, Measurement::value, Double::sum, TreeMap::new));
    }
}
