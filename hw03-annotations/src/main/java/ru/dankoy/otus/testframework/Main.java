package ru.dankoy.otus.testframework;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import ru.dankoy.otus.annotations.After;
import ru.dankoy.otus.annotations.Before;
import ru.dankoy.otus.annotations.Test;

public class Main {

  public static void main(String args[]) throws NoSuchMethodException {

    List<TripletRunner> tripletRunners = getTestsToRun(SimpleUnitTest.class);
    System.out.println(tripletRunners);

  }

  private static List<TripletRunner> getTestsToRun(Class classToTest) {

    Class<?> clazz = classToTest;

    // Получение методов
    Method[] methods = clazz.getDeclaredMethods();

    List<Method> beforeMethodsList = new ArrayList<>();
    List<Method> testMethodsList = new ArrayList<>();
    List<Method> afterMethodsList = new ArrayList<>();

    for (Method method : methods) {
      Annotation[] annotations = method.getDeclaredAnnotations();
      for (Annotation annotation : annotations) {
        if (annotation.annotationType().equals(Before.class)) {
          beforeMethodsList.add(method);
        } else if (annotation.annotationType().equals(Test.class)) {
          testMethodsList.add(method);
        } else if (annotation.annotationType().equals(After.class)) {
          afterMethodsList.add(method);
        }
      }
    }

    List<TripletRunner> tripletRunners = new ArrayList<>();
    for (Method method : testMethodsList) {
      tripletRunners.add(new TripletRunner(beforeMethodsList, method, afterMethodsList));
    }

    return tripletRunners;


  }

}
