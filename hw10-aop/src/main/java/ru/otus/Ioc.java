package ru.otus;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

class Ioc {

    private Ioc() {}

    static <T, T1 extends T> T createMyClass(T1 t1, Class<T> clazzInterface) {
        InvocationHandler handler = new DemoInvocationHandler<T>(t1);
        Object result = Proxy.newProxyInstance(Ioc.class.getClassLoader(), new Class<?>[] {clazzInterface}, handler);
        return clazzInterface.cast(result);
    }

    static <T, T1 extends T> T createMyClass(Class<T1> clazzResult, Class<T> clazzInterface)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        InvocationHandler handler =
                new DemoInvocationHandler<T>(clazzResult.getConstructor().newInstance());
        Object result = Proxy.newProxyInstance(Ioc.class.getClassLoader(), new Class<?>[] {clazzInterface}, handler);
        return clazzInterface.cast(result);
    }

    static class DemoInvocationHandler<T> implements InvocationHandler {
        private final T myClass;

        DemoInvocationHandler(T myClass) {
            this.myClass = myClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Annotation[] annotations = myClass.getClass()
                    .getDeclaredMethod(method.getName(), method.getParameterTypes())
                    .getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Log) {
                    System.out.print("method: " + method.getName() + ", param: ");
                    for (Object arg : args) {
                        System.out.print(arg);
                        System.out.print(" ");
                    }
                    System.out.println();
                }
            }
            return method.invoke(myClass, args);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" + "myClass=" + myClass + '}';
        }
    }
}
