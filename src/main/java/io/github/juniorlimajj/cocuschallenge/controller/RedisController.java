package io.github.juniorlimajj.cocuschallenge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
public class RedisController {

  private final RedisTemplate<String, Object> redisTemplate;
  @Autowired
  public RedisController(final RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @PostMapping("/kill-redis-cache")
  public void cleanCache() {
    this.redisTemplate.getConnectionFactory().getConnection().flushAll();
  }
}
