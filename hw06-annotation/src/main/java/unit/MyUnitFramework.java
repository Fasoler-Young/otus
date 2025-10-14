package unit;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import unit.annotation.After;
import unit.annotation.Before;
import unit.annotation.Test;

public class MyUnitFramework {

    public static void runAllTests(String className)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException,
                    IllegalAccessException {
        runAllTests(Class.forName(className));
    }

    public static void runAllTests(Class<?> clazz)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<Method> before = new ArrayList<>();
        List<Method> tests = new ArrayList<>();
        List<Method> after = new ArrayList<>();

        // Сортируем методы по аннотациям
        for (Method method : clazz.getDeclaredMethods()) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Before) {
                    before.add(method);
                } else if (annotation instanceof Test) {
                    tests.add(method);
                } else if (annotation instanceof After) {
                    after.add(method);
                }
            }
        }

        int success = 0;
        int fail = 0;
        // Запуск каждого теста требует успешного выполнения методов before,
        // однако не требует выполнения метод after
        // (хотя при возникновении ошибки в методе after, тест считается не пройденным),
        // поэтому при любой ошибке переходим к следующему тесту
        for (Method test : tests) {
            System.out.println("---------------------------");
            Object obj = clazz.getConstructor().newInstance();
            try {
                runMethods(obj, before);
                runMethod(obj, test);
                runMethods(obj, after);
                System.out.println("success: " + test.getName());
                success++;
            } catch (InvocationTargetException e) {
                Throwable targetException = e.getTargetException();
                System.out.println("fail: " + targetException.getMessage());
                System.out.println(Arrays.toString(targetException.getStackTrace()));
                fail++;
            } catch (Exception e) {
                System.out.println("fail: " + test.getName());
                System.out.println(e);
                fail++;
            }
        }
        System.out.println("---------------------------");
        System.out.println("success: " + success);
        System.out.println("fail: " + fail);
    }

    private static void runMethods(Object o, List<Method> methods)
            throws InvocationTargetException, IllegalAccessException {
        for (Method method : methods) {
            runMethod(o, method);
        }
    }

    private static void runMethod(Object o, Method method) throws InvocationTargetException, IllegalAccessException {
        method.setAccessible(true);
        method.invoke(o);
    }
}
