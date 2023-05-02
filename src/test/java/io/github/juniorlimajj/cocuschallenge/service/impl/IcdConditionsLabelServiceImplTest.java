package io.github.juniorlimajj.cocuschallenge.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import io.github.juniorlimajj.cocuschallenge.controller.LabelController;
import java.util.ArrayList;
import java.util.List;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import io.github.juniorlimajj.cocuschallenge.entity.IcdConditionsLabel;
import io.github.juniorlimajj.cocuschallenge.repository.IcdConditionsLabelRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class IcdConditionsLabelServiceImplTest {

  @Mock
  private IcdConditionsLabelRepository icdConditionsLabelRepository;

  @Mock
  private RedisTemplate<String, Object> redisTemplate;

  @Mock
  private ValueOperations<String, Object> valueOperations;

  private IcdConditionsLabelServiceImpl icdConditionsLabelServiceImpl;

  @BeforeEach
  public void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
    this.icdConditionsLabelServiceImpl = new IcdConditionsLabelServiceImpl(
        this.icdConditionsLabelRepository, this.redisTemplate);
    when(this.redisTemplate.opsForValue()).thenReturn(this.valueOperations);
  }

  @Test
  public void testGetAllLabels_CacheMiss() {
    final List<IcdConditionsLabel> expectedLabels = new ArrayList<>();
    final IcdConditionsLabel icdConditionsLabel1 = IcdConditionsLabel.builder()
        .id(1L)
        .code("label1")
        .build();
    final IcdConditionsLabel icdConditionsLabel2 = IcdConditionsLabel.builder()
        .id(2L)
        .code("label2")
        .build();
    expectedLabels.add(icdConditionsLabel1);
    expectedLabels.add(icdConditionsLabel2);

    when(this.redisTemplate.opsForValue().get(anyString())).thenReturn(null);
    when(this.icdConditionsLabelRepository.findAll()).thenReturn(expectedLabels);
    final ResponseEntity<List<IcdConditionsLabel>> response = this.icdConditionsLabelServiceImpl.getAllLabels();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expectedLabels, response.getBody());
    verify(this.redisTemplate.opsForValue()).get("allLabels");
  }

  @Test
  public void testGetAllLabels_CacheHit() {
    final List<IcdConditionsLabel> expectedLabels = new ArrayList<>();
    final IcdConditionsLabel icdConditionsLabel1 = IcdConditionsLabel.builder()
        .id(1L)
        .code("label1")
        .build();
    final IcdConditionsLabel icdConditionsLabel2 = IcdConditionsLabel.builder()
        .id(2L)
        .code("label2")
        .build();
    expectedLabels.add(icdConditionsLabel1);
    expectedLabels.add(icdConditionsLabel2);

    when(this.redisTemplate.opsForValue().get(anyString())).thenReturn(expectedLabels);
    final ResponseEntity<List<IcdConditionsLabel>> response = this.icdConditionsLabelServiceImpl.getAllLabels();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expectedLabels, response.getBody());
  }

  @Test
  public void testGetAllLabels_Error() {
    when(this.redisTemplate.opsForValue().get(anyString())).thenThrow(new RuntimeException("Redis error"));
    final ResponseEntity<List<IcdConditionsLabel>> response = this.icdConditionsLabelServiceImpl.getAllLabels();

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  public void testCreateIcdConditionsLabel() {
    final IcdConditionsLabel icdConditionsLabel = IcdConditionsLabel.builder()
        .id(1L)
        .code("code")
        .codeDescription("description")
        .build();
    final IcdConditionsLabel savedIcdConditionsLabel = IcdConditionsLabel.builder()
        .id(1L)
        .code("code")
        .codeDescription("description")
        .build();
    when(this.icdConditionsLabelRepository.save(any(IcdConditionsLabel.class))).thenReturn(savedIcdConditionsLabel);
    final ResponseEntity<IcdConditionsLabel> response = this.icdConditionsLabelServiceImpl.createIcdConditionsLabel(icdConditionsLabel);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(savedIcdConditionsLabel, response.getBody());
  }

  @Test
  public void testGetLabel() {
    final Long id = 1L;
    final IcdConditionsLabel label = new IcdConditionsLabel();
    label.setId(id);
    when(this.icdConditionsLabelRepository.findById(id)).thenReturn(Optional.of(label));

    final IcdConditionsLabel result = this.icdConditionsLabelServiceImpl.getLabel(id);

    assertNotNull(result);
    assertEquals(id, result.getId());
  }

  @Test
  public void testGetLabelNotFound() {
    final Long id = 1L;
    when(this.icdConditionsLabelRepository.findById(id)).thenReturn(Optional.empty());

    final IcdConditionsLabel result = this.icdConditionsLabelServiceImpl.getLabel(id);

    assertNull(result);
  }
}
