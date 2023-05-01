package io.github.juniorlimajj.cocuschallenge.service.impl;

import io.github.juniorlimajj.cocuschallenge.entity.IcdConditionsLabel;
import io.github.juniorlimajj.cocuschallenge.repository.IcdConditionsLabelRepository;
import io.github.juniorlimajj.cocuschallenge.service.IcdConditionsLabelService;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class IcdConditionsLabelServiceImpl implements IcdConditionsLabelService {

  private final IcdConditionsLabelRepository icdConditionsLabelRepository;
  private final RedisTemplate<String, Object> redisTemplate;
  private static final Logger logger = LoggerFactory.getLogger(IcdConditionsLabelServiceImpl.class);
  private static final String ALL_LABELS_CACHE_KEY = "allLabels";
  static final long ALL_LABELS_TTL_SECONDS = 3600; // 1 hour

  @Autowired
  public IcdConditionsLabelServiceImpl(final IcdConditionsLabelRepository icdConditionsLabelRepository,
      final RedisTemplate<String, Object> redisTemplate) {
    this.icdConditionsLabelRepository = icdConditionsLabelRepository;
    this.redisTemplate = redisTemplate;
  }

  @Override
  public ResponseEntity<List<IcdConditionsLabel>> getAllLabels() {
    try {
      logger.info("Retrieving all labels from Redis cache with key '{}'", ALL_LABELS_CACHE_KEY);
      List<IcdConditionsLabel> labels = (List<IcdConditionsLabel>) this.redisTemplate.opsForValue().get(ALL_LABELS_CACHE_KEY);
      if (labels == null) {
        logger.info("Cache miss. Retrieving all labels from database");
        labels = this.icdConditionsLabelRepository.findAll();
        this.redisTemplate.opsForValue().set(ALL_LABELS_CACHE_KEY, labels, ALL_LABELS_TTL_SECONDS, TimeUnit.SECONDS);
        logger.info("Stored all labels in Redis cache with key '{}' and TTL of {} seconds", ALL_LABELS_CACHE_KEY, ALL_LABELS_TTL_SECONDS);
      } else {
        logger.info("Retrieved all labels from Redis cache with key '{}'", ALL_LABELS_CACHE_KEY);
      }
      return new ResponseEntity<>(labels, HttpStatus.OK);
    } catch (final Exception e) {
      logger.error("Error while getting all labels", e);
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
