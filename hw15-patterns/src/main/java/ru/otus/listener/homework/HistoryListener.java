package ru.otus.listener.homework;

import java.util.*;
import ru.otus.listener.Listener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

public class HistoryListener implements Listener, HistoryReader {

    Map<Long, Message> history = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        List<String> data = new ArrayList<>(msg.getField13().getData());
        ObjectForMessage field13 = new ObjectForMessage();
        field13.setData(data);
        history.put(msg.getId(), msg.toBuilder().field13(field13).build());
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(history.get(id));
    }
}
