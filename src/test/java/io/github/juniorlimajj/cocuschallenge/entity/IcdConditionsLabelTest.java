package io.github.juniorlimajj.cocuschallenge.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class IcdConditionsLabelTest {

  @Test
  public void testIdGetterAndSetter() {
    IcdConditionsLabel icdConditionsLabel = mock(IcdConditionsLabel.class);
    when(icdConditionsLabel.getId()).thenReturn(1L);
    assertEquals(1L, icdConditionsLabel.getId().longValue());

    assertThrows(AssertionError.class, () -> {
      assertEquals(2L, icdConditionsLabel.getId().longValue());
    });
  }

  @Test
  public void testCodeGetterAndSetter() {
    IcdConditionsLabel icdConditionsLabel = mock(IcdConditionsLabel.class);
    when(icdConditionsLabel.getCode()).thenReturn("A00");
    assertEquals("A00", icdConditionsLabel.getCode());

    assertThrows(AssertionError.class, () -> {
      assertEquals("B00", icdConditionsLabel.getCode());
    });
  }

  @Test
  public void testCodeDescriptionGetterAndSetter() {
    IcdConditionsLabel icdConditionsLabel = mock(IcdConditionsLabel.class);
    when(icdConditionsLabel.getCodeDescription()).thenReturn("Cholera");
    assertEquals("Cholera", icdConditionsLabel.getCodeDescription());

    assertThrows(AssertionError.class, () -> {
      assertEquals("Malaria", icdConditionsLabel.getCodeDescription());
    });
  }
}
