package ru.otus.listener.homework;

import java.util.*;
import ru.otus.listener.Listener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, Message> history = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        Message historyMsg;
        if (msg == null) {
            return;
        } else if (msg.getField13() != null) {
            List<String> data = new ArrayList<>(msg.getField13().getData());
            ObjectForMessage field13 = new ObjectForMessage();
            field13.setData(data);
            historyMsg = msg.toBuilder().field13(field13).build();
        } else {
            historyMsg = msg.toBuilder().build();
        }

        history.put(msg.getId(), historyMsg);
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(history.get(id));
    }
}
