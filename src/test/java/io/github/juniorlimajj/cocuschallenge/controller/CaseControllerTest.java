package io.github.juniorlimajj.cocuschallenge.controller;

import io.github.juniorlimajj.cocuschallenge.entity.Case;
import io.github.juniorlimajj.cocuschallenge.service.CaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
class CaseControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CaseService caseService;

  @MockBean
  private TestRestTemplate restTemplate;

  private List<Case> caseList;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    this.caseList = new ArrayList<>();
    final Case case1 = new Case(1L, "Test Case 1", null, null, "30min");
    final Case case2 = new Case(2L, "Test Case 2", null, null, "30min");
    this.caseList.add(case1);
    this.caseList.add(case2);
  }

  @Test
  void testGetAllCases() throws Exception {
    when(this.caseService.getAllCases()).thenReturn(ResponseEntity.ok(this.caseList));
    this.mockMvc.perform(MockMvcRequestBuilders.get("/api/case/get/all")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("Test Case 1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value("Test Case 2"));
  }

  @Test
  void testGetCaseById() throws Exception {
    when(this.caseService.getCaseById(1L)).thenReturn(ResponseEntity.ok(this.caseList.get(0)));
    this.mockMvc.perform(MockMvcRequestBuilders.get("/api/case/get/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Test Case 1"));
    }
}