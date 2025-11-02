package ru.otus;

public class TestLogging implements TestLoggingInterface {

    @Log
    @Override
    public void calculation(int param) {
        System.out.println("calculation");
    }

    public TestLogging() {}

    @Override
    public void calculation(int param, int param2) {
        System.out.println("calculation2");
    }
}
