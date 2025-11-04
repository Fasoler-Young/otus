package ru.otus;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        private final Set<MethodMeta> needLogging;

        record MethodMeta(String name, List<Class<?>> parameterTypes) {}

        DemoInvocationHandler(T myClass) {
            this.myClass = myClass;
            needLogging = new HashSet<>();
            for (Method method : myClass.getClass().getMethods()) {
                for (Annotation annotation : method.getAnnotations()) {
                    if (annotation instanceof Log) {
                        needLogging.add(new MethodMeta(method.getName(), Arrays.asList(method.getParameterTypes())));
                        break;
                    }
                }
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            if (needLogging.contains(new MethodMeta(method.getName(), Arrays.asList(method.getParameterTypes())))) {
                System.out.print("method: " + method.getName() + ", param: ");
                for (Object arg : args) {
                    System.out.print(arg);
                    System.out.print(" ");
                }
                System.out.println();
            }
            return method.invoke(myClass, args);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" + "myClass=" + myClass + '}';
        }
    }
}
