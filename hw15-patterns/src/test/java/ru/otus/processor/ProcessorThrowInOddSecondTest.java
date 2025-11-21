package ru.otus.processor;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;

class ProcessorThrowInOddSecondTest {

    @Test
    void process() {
        Message message = new Message.Builder(123).build();

        DateTimeProvider oddSecond = () -> LocalDateTime.of(2020, 1, 1, 0, 0, 0);
        Processor processorThrowInOddSecond = new ProcessorThrowInOddSecond(oddSecond);
        DateTimeProvider notOddSecond = () -> LocalDateTime.of(2020, 1, 1, 0, 0, 1);
        Processor processorThrowInOddSecond2 = new ProcessorThrowInOddSecond(notOddSecond);

        assertThrows(IllegalStateException.class, () -> processorThrowInOddSecond.process(message));
        assertDoesNotThrow(() -> processorThrowInOddSecond2.process(message));
    }
}
