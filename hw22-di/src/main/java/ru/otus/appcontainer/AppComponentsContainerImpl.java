package ru.otus.appcontainer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import org.reflections.Reflections;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?>... initialConfigClass) {
        Arrays.stream(initialConfigClass)
                .peek(this::checkConfigClass)
                .sorted(getAppComponentsContainerComparator())
                .forEach(this::processConfig);
    }


    public AppComponentsContainerImpl(String package_) {
        Reflections reflections = new Reflections(package_);
        reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class).stream()
                .sorted(getAppComponentsContainerComparator())
                .forEach(this::processConfig);
    }

    private static Comparator<Class<?>> getAppComponentsContainerComparator() {
        return Comparator.comparingInt(config ->
                config.getAnnotation(AppComponentsContainerConfig.class).order());
    }

    private void processConfig(Class<?> configClass) {
        // Собираем только методы помеченные AppComponent и сортируем по порядку
        Method[] declaredMBeans = Arrays.stream(configClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(
                        method -> method.getAnnotation(AppComponent.class).order()))
                .toArray(Method[]::new);
        // Создаем объект конфигурации для выполнения методов
        Object configurationClass;
        try {
            configurationClass = configClass.getConstructor().newInstance();
        } catch (InstantiationException
                | IllegalAccessException
                | InvocationTargetException
                | NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
        for (Method method : declaredMBeans) {
            // Определяем имя компонента из аннотации или имени метода
            String nameFromAnnotation = method.getAnnotation(AppComponent.class).name();
            String componentName = nameFromAnnotation == null
                    ? method.getName()
                    : nameFromAnnotation;
            if (appComponentsByName.containsKey(componentName)) {
                throw new IllegalArgumentException("Duplicate component name " + componentName);
            }
            // Проверка возвращаемого типа
            if (method.getReturnType() == void.class) {
                throw new IllegalArgumentException(
                        String.format("@AppComponent method %s should not return void", method.getName()));
            }
            // Если компонент требует уже созданные компоненты, ищем их
            Object[] params = Arrays.stream(method.getParameterTypes())
                    .map(this::getAppComponent)
                    .toArray();
            try {
                Object newComponent = method.invoke(configurationClass, params);
                appComponentsByName.put(componentName, newComponent);
                appComponents.add(newComponent);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        C param = null;
        for (Object appComponent : appComponents) {
            if (componentClass.isAssignableFrom(appComponent.getClass())) {
                if (param != null) {
                    throw new IllegalArgumentException("Impossible determine appropriate component");
                }
                param = componentClass.cast(appComponent);
            }
        }
        if (param == null) {
            throw new IllegalArgumentException("Appropriate component not found");
        }
        return param;
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) Optional.ofNullable(appComponentsByName.get(componentName))
                .orElseThrow(() -> new IllegalArgumentException("Appropriate component not found"));
    }
}
