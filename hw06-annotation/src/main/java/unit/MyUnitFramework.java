package unit;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
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
        // Запуск каждого теста требует успешного выполнения методов before.
        // Методы after выполняются в любом случае.
        // Тест считается успешным если нет ошибок ни на каком этапе.
        for (Method test : tests) {
            System.out.println("---------------------------");
            Object obj = clazz.getConstructor().newInstance();

            boolean hasErrorBefore = runMethods(obj, before);
            boolean hasError = false;
            if (!hasErrorBefore) {
                hasError = runMethod(obj, test);
            }
            boolean hasErrorAfter = runMethods(obj, after);
            if (!hasErrorBefore && !hasError && !hasErrorAfter) {
                System.out.println("success: " + test.getName());
                success++;
            } else {
                fail++;
                System.out.println("fail: " + test.getName());
            }
        }
        System.out.println("---------------------------");
        System.out.println("success: " + success);
        System.out.println("fail: " + fail);
    }

    private static boolean runMethods(Object o, List<Method> methods) {
        boolean hasError = false;
        for (Method method : methods) {
            boolean result = runMethod(o, method);
            if (!hasError && result) {
                hasError = true;
            }
        }
        return hasError;
    }

    private static boolean runMethod(Object o, Method method) {
        try {
            method.setAccessible(true);
            method.invoke(o);
            return false;
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            System.out.println("error: " + targetException.getMessage());
            System.out.println(Arrays.toString(targetException.getStackTrace()));
        } catch (Exception e) {
            System.out.println("error: " + method.getName());
            System.out.println(e);
        }
        return true;
    }
}
