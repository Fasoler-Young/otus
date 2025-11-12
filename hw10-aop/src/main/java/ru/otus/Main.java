package ru.otus;

import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args)
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        TestLoggingInterface myClass = Ioc.createMyClass(TestLogging.class, TestLoggingInterface.class);
        myClass.calculation(6);
        myClass.calculation(7, 8);
        TestLoggingInterface myClass2 = Ioc.createMyClass(new TestLogging(), TestLoggingInterface.class);
        myClass2.calculation(9);
        myClass2.calculation(10, 11);
    }
}
