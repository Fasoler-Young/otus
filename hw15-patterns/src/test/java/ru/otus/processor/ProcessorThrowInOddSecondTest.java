package ru.otus.processor;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import ru.otus.model.Message;

class ProcessorThrowInOddSecondTest {

    @Test
    void process() throws InterruptedException {
        Processor processorThrowInOddSecond = new ProcessorThrowInOddSecond();

        if (System.currentTimeMillis() / 1000 % 2 == 0) {
            Thread.sleep(1000);
        }

        Message message = new Message.Builder(123).build();

        processorThrowInOddSecond.process(message);

        Thread.sleep(1000);

        assertThrows(IllegalStateException.class, () -> processorThrowInOddSecond.process(message));
    }
}
