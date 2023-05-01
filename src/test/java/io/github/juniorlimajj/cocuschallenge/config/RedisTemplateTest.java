package io.github.juniorlimajj.cocuschallenge.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTemplateTest {

  @Autowired
  private RedisTemplate<String, Object> redisTemplate;

  @Test
  public void testRedisTemplate() {
    final String key = "testKey";
    final String value = "testValue";
    final long ttl = 1L;
    this.redisTemplate.opsForValue().set(key, value, ttl, TimeUnit.SECONDS);
    Object retrievedValue = this.redisTemplate.opsForValue().get(key);
    assertNotNull(retrievedValue);
    assertEquals(value, retrievedValue.toString());
    try {
      Thread.sleep(TimeUnit.SECONDS.toMillis(ttl + 1));
    } catch (final InterruptedException e) {
      e.printStackTrace();
    }
    retrievedValue = this.redisTemplate.opsForValue().get(key);
    assertNull(retrievedValue);
  }
}

