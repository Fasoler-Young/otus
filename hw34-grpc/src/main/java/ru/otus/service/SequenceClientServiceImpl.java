package ru.otus.service;

import ru.otus.protobuf.SequenceCurValue;

public class SequenceClientServiceImpl implements SequenceClientService {

    private int curValue;

    @Override
    public void onNext(SequenceCurValue sequenceCurValue) {
        this.curValue = (int) sequenceCurValue.getValue();
        System.out.println("new value: " + this.curValue);
    }

    @Override
    public void onError(Throwable throwable) {
        System.err.println(throwable.getMessage());
    }

    @Override
    public void onCompleted() {
        System.out.println("\n\nЯ все!");
    }

    public synchronized int getCurValueAndClear() {
        int curValue = this.curValue;
        this.curValue = 0;
        return curValue;
    }
}
