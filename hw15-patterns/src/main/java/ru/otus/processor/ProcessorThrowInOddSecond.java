package ru.otus.processor;

import ru.otus.model.Message;

public class ProcessorThrowInOddSecond implements Processor {

    private final DateTimeProvider dateTimeProvider;

    public ProcessorThrowInOddSecond(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        if (dateTimeProvider.getDate().getSecond() % 2 == 0) {
            throw new IllegalStateException("Четная секунда");
        }

        return message;
    }
}
