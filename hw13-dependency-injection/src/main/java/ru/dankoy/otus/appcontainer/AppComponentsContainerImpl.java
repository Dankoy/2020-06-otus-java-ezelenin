package ru.dankoy.otus.appcontainer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dankoy.otus.appcontainer.api.AppComponent;
import ru.dankoy.otus.appcontainer.api.AppComponentsContainer;
import ru.dankoy.otus.appcontainer.api.AppComponentsContainerConfig;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private static final Logger logger = LoggerFactory.getLogger(AppComponentsContainerImpl.class);

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        final Object instance = initInstanceConstructor(configClass);
        List<Method> orderedMethodList = getMethods(configClass);
        initComponents(instance, orderedMethodList);
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

  @Override
  public <C> C getAppComponent(Class<C> componentClass) {

    Optional<Object> optionalComponent =
        appComponents.stream()
            .filter(object -> componentClass.isAssignableFrom(object.getClass()))
            .reduce((u, v) -> {
              throw new IllegalStateException("More than one ID found");
            });

    if (optionalComponent.isEmpty()) {
      throw new AppComponentContainerException(
          String.format("Component '%s' is not in context",
              componentClass));
    } else {
      return (C) optionalComponent.get();
    }
  }

    /**
     * Ищет объекты\компоненты в контексте, от которых могут зависеть другие компоненты
     *
     * @param componentName
     * @param <C>
     * @return
     */
    @Override
    public <C> C getAppComponent(String componentName) {
        C component = (C) appComponentsByName.get(componentName);
        if (Objects.isNull(component)) {
            throw new AppComponentContainerException(String.format("Component '%s' is not in context",
                    componentName));
        }
        return component;
    }


    /**
     * Получает список методов с нужной аннотацией и сортирует их пополю order в аннотации @AppComponent
     *
     * @param clazz
     * @return
     */
    private List<Method> getMethods(Class<?> clazz) {

        final Method[] methods = clazz.getDeclaredMethods();

        return Arrays.stream(methods).filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparing(method -> method.getAnnotation(AppComponent.class).order()))
                .collect(Collectors.toList());

    }

    /**
     * Получает массив из объектов аргументов метода
     *
     * @param method
     * @return
     */
    private Object[] getMethodArguments(Method method) {

        Object[] objects = new Object[method.getParameterCount()];

        Parameter[] parameters = method.getParameters();

        for (int i = 0; i < parameters.length; i++) {
            objects[i] = getAppComponent(parameters[i].getType());
        }
        return objects;
    }


    /**
     * Получает объект типа класса передаваемого в аргументах
     *
     * @param clazz
     * @return
     */
    private Object initInstanceConstructor(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw new AppComponentContainerException(
                    String.format("Failed to instantiate class %s", clazz.getCanonicalName()));
        }
    }

    /**
     * Производит инициализацию компонентов конфигурационного класса
     *
     * @param objectInstance
     * @param methods
     */
    private void initComponents(Object objectInstance, List<Method> methods) {

        for (Method method : methods) {
            final AppComponent componentInfo = method.getAnnotation(AppComponent.class);
            logger.info("Method: {}", method.getName());
            try {
                method.setAccessible(true);
                Object component = method.invoke(objectInstance, getMethodArguments(method));
                appComponents.add(component);
                appComponentsByName.put(componentInfo.name(), component);
            } catch (Exception e) {
                logger.error("Error: {}", e.getMessage());
                logger.error("Error: {}", e);
                throw new AppComponentContainerException(
                        String.format("Cannot create component '%s'", componentInfo.name()));
            }

        }

    }


}
