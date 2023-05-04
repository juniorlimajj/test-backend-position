package io.github.juniorlimajj.cocuschallenge.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.github.juniorlimajj.cocuschallenge.entity.IcdConditionsLabel;
import io.github.juniorlimajj.cocuschallenge.service.IcdConditionsLabelService;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(LabelController.class)
public class LabelControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private IcdConditionsLabelService icdConditionsLabelService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testGetAllIcdConditionsLabels() throws Exception {
    final List<IcdConditionsLabel> labels = Collections.singletonList(new IcdConditionsLabel(
        1L, "label1","label1 desc",null));
    final ResponseEntity<List<IcdConditionsLabel>> response = new ResponseEntity<>(labels, HttpStatus.OK);
    final String url = "/api/label/get/all";

    when(this.icdConditionsLabelService.getAllLabels()).thenReturn(response);

    this.mockMvc.perform(MockMvcRequestBuilders.get(url))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.jsonPath("[0].id", equalTo(1)))
        .andExpect(MockMvcResultMatchers.jsonPath("[0].code", equalTo("label1")));
  }


  @Test
  public void testCreateLabel() {
    final IcdConditionsLabel icdConditionsLabel = new IcdConditionsLabel();
    final ResponseEntity<IcdConditionsLabel> expectedResponse = new ResponseEntity<>(icdConditionsLabel, HttpStatus.CREATED);
    when(this.icdConditionsLabelService.createIcdConditionsLabel(any(IcdConditionsLabel.class))).thenReturn(expectedResponse);
    final ResponseEntity<IcdConditionsLabel> response = this.icdConditionsLabelService.createIcdConditionsLabel(icdConditionsLabel);
    verify(this.icdConditionsLabelService).createIcdConditionsLabel(icdConditionsLabel);
    assertEquals(expectedResponse, response);
  }

  @Test
  public void testGetLabelById() {
    final long id = 1L;
    final IcdConditionsLabel icdConditionsLabel = new IcdConditionsLabel();
    icdConditionsLabel.setId(id);
    final ResponseEntity<IcdConditionsLabel> expectedResponse = ResponseEntity.ok(icdConditionsLabel);

    when(this.icdConditionsLabelService.getLabelById(id)).thenReturn(expectedResponse);

    final ResponseEntity<IcdConditionsLabel> actualResponse = this.icdConditionsLabelService.getLabelById(id);

    assertEquals(expectedResponse, actualResponse);
  }

  @Test
  public void deleteLabel_shouldCallServiceToDeleteLabel() {
    final long id = 1L;
    final ResponseEntity<IcdConditionsLabel> expectedResponse = ResponseEntity.ok().build();
    when(this.icdConditionsLabelService.deleteIcdConditionsLabel(id)).thenReturn(expectedResponse);

    final ResponseEntity<IcdConditionsLabel> actualResponse = this.icdConditionsLabelService.deleteIcdConditionsLabel(id);

    verify(this.icdConditionsLabelService).deleteIcdConditionsLabel(id);
    assertEquals(expectedResponse, actualResponse);
  }

  @Test
  public void updateLabel_shouldCallServiceToUpdateLabel() {
    final long id = 1L;
    final IcdConditionsLabel icdConditionsLabel = new IcdConditionsLabel();
    final ResponseEntity<IcdConditionsLabel> expectedResponse = ResponseEntity.ok().body(icdConditionsLabel);
    when(this.icdConditionsLabelService.updateIcdConditionsLabel(id, icdConditionsLabel)).thenReturn(expectedResponse);

    final ResponseEntity<IcdConditionsLabel> actualResponse = this.icdConditionsLabelService.updateIcdConditionsLabel(id, icdConditionsLabel);

    verify(this.icdConditionsLabelService).updateIcdConditionsLabel(id, icdConditionsLabel);
    assertEquals(expectedResponse, actualResponse);
  }
}