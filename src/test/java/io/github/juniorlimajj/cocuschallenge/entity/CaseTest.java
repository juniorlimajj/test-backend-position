package io.github.juniorlimajj.cocuschallenge.entity;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CaseTest {

  @Test
  public void testGetId() {
    final Case c = new Case();
    c.setId(1L);
    assertEquals(1L, c.getId().longValue());
  }

  @Test
  public void testGetDescription() {
    final Case c = new Case();
    c.setDescription("Test description");
    assertEquals("Test description", c.getDescription());
  }

  @Test
  public void testGetDoctor() {
    final Doctor d = new Doctor();
    d.setId(1L);
    d.setName("Test doctor");
    final Case c = new Case();
    c.setDoctor(d);
    assertEquals(d, c.getDoctor());
  }

  @Test
  public void testGetIcdConditionsLabels() {
    final IcdConditionsLabel l1 = new IcdConditionsLabel();
    l1.setId(1L);
    l1.setCode("Label 1");
    final IcdConditionsLabel l2 = new IcdConditionsLabel();
    l2.setId(2L);
    l2.setCode("Label 2");
    final Case c = new Case();
    final List<IcdConditionsLabel> labels = new ArrayList<>();
    labels.add(l1);
    labels.add(l2);
    c.setIcdConditionsLabels(labels);
    c.setDoctor(new Doctor(1L, "Jose Junior",null));
    assertEquals(2, c.getIcdConditionsLabels().size());
  }

  @Test
  public void testGetTimeToLabel() {
    final Case c = new Case();
    c.setTimeToLabel("Test time");
    assertEquals("Test time", c.getTimeToLabel());
  }
}