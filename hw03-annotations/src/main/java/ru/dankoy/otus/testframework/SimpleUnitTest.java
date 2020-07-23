package ru.dankoy.otus.testframework;

import ru.dankoy.otus.annotations.After;
import ru.dankoy.otus.annotations.Before;
import ru.dankoy.otus.annotations.Test;

public class SimpleUnitTest {

  @Before
  public void setUp() {
    System.out.println("Setup");
  }

  @Test
  public void method1Test() {
    System.out.println("Test1");
  }

  @Test
  public void method2Test() {
    System.out.println("Test2");
  }

  @Test
  public void method3Test() {
    System.out.println("Test3");
  }

  @Test
  public void method4Test() {
    System.out.println("Test4");
  }

  @Test
  public void method5Test() {
    System.out.println("Test5");
  }

  @After
  public void cleanUp() {
    System.out.println("CleanUp");
  }

}
