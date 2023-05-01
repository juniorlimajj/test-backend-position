package io.github.juniorlimajj.cocuschallenge.controller;

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
    final List<IcdConditionsLabel> labels = Collections.singletonList(new IcdConditionsLabel(1L, "label1","label1 desc"));
    final ResponseEntity<List<IcdConditionsLabel>> response = new ResponseEntity<>(labels, HttpStatus.OK);
    final String url = "/api/get/all/labels";

    when(this.icdConditionsLabelService.getAllLabels()).thenReturn(response);

    this.mockMvc.perform(MockMvcRequestBuilders.get(url))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.jsonPath("[0].id", equalTo(1)))
        .andExpect(MockMvcResultMatchers.jsonPath("[0].code", equalTo("label1")));
  }
}