package ru.dankoy.otus.testframework;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.dankoy.otus.annotations.After;
import ru.dankoy.otus.annotations.Before;
import ru.dankoy.otus.annotations.Test;
import ru.dankoy.otus.testclasses.SimpleUnitTest;

public class Main {

  public static void main(String args[])
      throws InvocationTargetException, IllegalAccessException {

    List<TripletRunner> tripletRunners = getTestsToRun(SimpleUnitTest.class);

    Map<String, Integer> results = runTests(tripletRunners);

    printTestStatuses(results);

  }

  /**
   * Собирает список из объектов TripletRunner в котором содержатся нужные методы помеченные
   * аннотациями @Before, @After и @Test
   *
   * @param classToTest
   * @return
   */
  private static List<TripletRunner> getTestsToRun(Class<?> classToTest) {

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
      tripletRunners.add(new TripletRunner(beforeMethodsList, method, afterMethodsList, clazz));
    }

    return tripletRunners;

  }

  /**
   * Метод прогоняет прогоняет все Before, Test, After методы, собранные в списке объектов
   * TripletRunner.
   * Возвращает статистику в виде Map
   *
   * @param testTripletObjects
   * @return
   * @throws InvocationTargetException
   * @throws IllegalAccessException
   */
  private static Map<String, Integer> runTests(List<TripletRunner> testTripletObjects)
      throws InvocationTargetException, IllegalAccessException {

    String out = "Method %s has completed successfully: %s \n";

    int successfulTests = 0;
    int failedTests = 0;
    Map<String, Integer> results = new HashMap<>();

    for (TripletRunner object : testTripletObjects) {
      System.out.println("---");
      object.runTest();
      if (object.getStatus()) {
        successfulTests++;
      } else {
        failedTests++;
      }
      System.out.println(String.format(out, object.getTest().getName(),object.getStatus()));
    }

    results.put("successfulTests", successfulTests);
    results.put("failedTests", failedTests);
    results.put("totalTests", failedTests + successfulTests);

    return results;

  }

  /***
   * Печатает общий статус прогона тестов
   *
   * @param results
   */
  private static void printTestStatuses(Map<String, Integer> results) {

    System.out.println("Successfully finished tests: " + results.get("successfulTests"));
    System.out.println("Failed tests: " + results.get("failedTests"));
    System.out.println("Total tests: " + results.get("totalTests"));

  }

}
