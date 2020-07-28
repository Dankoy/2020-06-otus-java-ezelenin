package ru.dankoy.otus.testframework;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Класс описывает объект с полями List<Method> before - список всех методов с аннотацией Before.
 * Так как таких методов может быть много и они все должны быть выполнены до выполнения теста -
 * потому передается лист. Method test - один метод с аннотацией @Test List<Method> after - список
 * методов с аннотацией @After. Так как таких методов может быть много и все они должны быть
 * выполнены после выполнения самого теста - поэтому передается лист boolean status - статус
 * завершения теста. Class<?> clazz - Передаваемый отрефлексированный класс.
 */
public class TripletRunner {

  private Class<?> clazz;
  private List<Method> before;
  private Method test;
  private List<Method> after;
  private boolean status;

  public List<Method> getBefore() {
    return before;
  }

  public void setBefore(List<Method> before) {
    this.before = before;
  }

  public Method getTest() {
    return test;
  }

  public void setTest(Method test) {
    this.test = test;
  }

  public List<Method> getAfter() {
    return after;
  }

  public void setAfter(List<Method> after) {
    this.after = after;
  }

  public boolean getStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "TripletRunner{" +
        "before=" + before +
        ", test=" + test +
        ", after=" + after +
        ", status=" + status +
        '}';
  }

  public TripletRunner() {

  }

  public TripletRunner(List<Method> before, Method test, List<Method> after, Class<?> clazz) {
    this.before = before;
    this.test = test;
    this.after = after;
    this.clazz = clazz;
  }

  public void runTest()
      throws InvocationTargetException, IllegalAccessException {

    // Создание объекта тестирования
    Object object = ReflectionHelper.instantiate(clazz);

    // Run all before methods
    for (Method beforeMethod : before) {
      beforeMethod.invoke(object);
    }

    // Run test
    try {
      test.invoke(object);
      status = true;
    } catch (Exception e) {
      status = false;
    }

    // Run all After methods
    for (Method afterMethod : after) {
      afterMethod.invoke(object);
    }

  }

}
