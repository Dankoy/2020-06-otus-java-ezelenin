package ru.dankoy.otus.testclasses;

import static org.assertj.core.api.Assertions.*;
import ru.dankoy.otus.annotations.After;
import ru.dankoy.otus.annotations.Before;
import ru.dankoy.otus.annotations.Test;

public class SimpleUnitTest {

  @Before
  public void setUp1() {
    System.out.println("Setup1");
  }

  @Before
  public void setUp2() {
    System.out.println("Setup2");
  }

  @Test
  public void method1Test() {
    System.out.println("method1Test");
    assertThat(1).isEqualTo(1);
  }

  @Test
  public void method2Test() {
    System.out.println("method2Test");
    assertThat(1).isEqualTo(2);
  }

  @Test
  public void method3Test() {
    System.out.println("method3Test");
    assertThat(1).isEqualTo(1);
  }

  @Test
  public void method4Test() {
    System.out.println("method4Test");
  }

  @Test
  public void method5Test() {
    System.out.println("method5Test");
    assertThat(1).isEqualTo(2);
  }

  @After
  public void cleanUp1() {
    System.out.println("CleanUp1");
  }

  @After
  public void cleanUp2() {
    System.out.println("CleanUp2");
  }

}
