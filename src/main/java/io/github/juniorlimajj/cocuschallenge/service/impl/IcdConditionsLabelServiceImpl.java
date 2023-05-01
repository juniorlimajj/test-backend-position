package io.github.juniorlimajj.cocuschallenge.service.impl;

import io.github.juniorlimajj.cocuschallenge.entity.IcdConditionsLabel;
import io.github.juniorlimajj.cocuschallenge.repository.IcdConditionsLabelRepository;
import io.github.juniorlimajj.cocuschallenge.service.IcdConditionsLabelService;
import java.util.List;
import java.util.Optional;
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
  private static final String LABEL_BY_ID_CACHE_KEY = "getLabelById";
  static final long TTL_SECONDS = 3600; // 1 hour

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
        this.redisTemplate.opsForValue().set(ALL_LABELS_CACHE_KEY, labels, TTL_SECONDS, TimeUnit.SECONDS);
        logger.info("Stored all labels in Redis cache with key '{}' and TTL of {} seconds", ALL_LABELS_CACHE_KEY, TTL_SECONDS);
      } else {
        logger.info("Retrieved all labels from Redis cache with key '{}'", ALL_LABELS_CACHE_KEY);
      }
      return new ResponseEntity<>(labels, HttpStatus.OK);
    } catch (final Exception e) {
      logger.error("Error while getting all labels", e);
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public ResponseEntity<IcdConditionsLabel> createIcdConditionsLabel(final IcdConditionsLabel icdConditionsLabel) {
    logger.info("Saving data id :'{}', code: '{}', description: '{}'", icdConditionsLabel.getId(), icdConditionsLabel.getCode(), icdConditionsLabel.getCodeDescription());
    final IcdConditionsLabel label = this.icdConditionsLabelRepository
        .save(IcdConditionsLabel.builder()
            .id(icdConditionsLabel.getId())
            .code(icdConditionsLabel.getCode())
            .codeDescription(icdConditionsLabel.getCodeDescription())
            .build());
    logger.info("Finished transaction...");
    return new ResponseEntity<>(label, HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<IcdConditionsLabel> getLabelById(final Long id) {
    try {
      final IcdConditionsLabel labelResultSet = this.getLabel(id);
      if (labelResultSet != null){
        logger.info("Getting label with ID '{}'...",id);
        return new ResponseEntity<>(labelResultSet, HttpStatus.OK);
      }
      logger.info("The label with the ID '{}' does not exist...",id);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (final Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public IcdConditionsLabel getLabel(final Long id) {
    final Optional<IcdConditionsLabel> labelObj = this.icdConditionsLabelRepository.findById(id);
    if (labelObj.isPresent()) {
      return labelObj.get();
    }
    return null;
  }
}
