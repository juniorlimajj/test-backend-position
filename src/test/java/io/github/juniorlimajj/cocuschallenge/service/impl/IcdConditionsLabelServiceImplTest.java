package io.github.juniorlimajj.cocuschallenge.service.impl;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import io.github.juniorlimajj.cocuschallenge.entity.IcdConditionsLabel;
import io.github.juniorlimajj.cocuschallenge.repository.IcdConditionsLabelRepository;

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

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals(expectedLabels, response.getBody());
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

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals(expectedLabels, response.getBody());
  }

  @Test
  @DisplayName("Get all labels - error")
  public void testGetAllLabels_Error() {
    when(this.redisTemplate.opsForValue().get(anyString())).thenThrow(new RuntimeException("Redis error"));
    final ResponseEntity<List<IcdConditionsLabel>> response = this.icdConditionsLabelServiceImpl.getAllLabels();

    Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    Assertions.assertNull(response.getBody());
  }
}
