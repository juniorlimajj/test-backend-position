/**
 * This class implements the IcdConditionsLabelService interface and provides methods to interact with the IcdConditionsLabelRepository and Redis cache.
 * It contains methods to retrieve all labels, create a new label, retrieve a label by ID, delete a label by ID, and get a label by ID.
 * The class also contains constants for Redis cache keys and TTL, and a logger for logging messages.
 */

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
  private static final String LABEL_BY_ID_CACHE_KEY = "label_";
  static final long TTL_SECONDS = 3600;

  /**
   * Constructor for IcdConditionsLabelServiceImpl class.
   * @param icdConditionsLabelRepository - IcdConditionsLabelRepository object
   * @param redisTemplate - RedisTemplate object
   */
  @Autowired
  public IcdConditionsLabelServiceImpl(final IcdConditionsLabelRepository icdConditionsLabelRepository,
      final RedisTemplate<String, Object> redisTemplate) {
    this.icdConditionsLabelRepository = icdConditionsLabelRepository;
    this.redisTemplate = redisTemplate;
  }

  /**
   * Retrieves all labels from Redis cache or database.
   * @return ResponseEntity object containing a list of IcdConditionsLabel objects and HTTP status code
   */
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
  /**
   * Creates a new IcdConditionsLabel object and saves it to the database.
   * @param icdConditionsLabel - IcdConditionsLabel object to be created
   * @return ResponseEntity object containing the created IcdConditionsLabel object and HTTP status code
   */
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
  /**
   * Retrieves a label by ID from Redis cache or database.
   * @param id - Long value representing the ID of the label to be retrieved
   * @return ResponseEntity object containing the retrieved IcdConditionsLabel object and HTTP status code
   */
  @Override
  public ResponseEntity<IcdConditionsLabel> getLabelById(final Long id) {
    try {
      final IcdConditionsLabel labelResultSet = (IcdConditionsLabel) this.redisTemplate.opsForValue().get(LABEL_BY_ID_CACHE_KEY + id);
      if (labelResultSet != null){
        logger.info("Getting label with ID '{}' from Redis cache...",id);
        return new ResponseEntity<>(labelResultSet, HttpStatus.OK);
      }
      logger.info("The label with the ID '{}' does not exist in Redis cache...",id);
      final IcdConditionsLabel labelFromDB = this.getLabel(id);
      if (labelFromDB != null) {
        this.redisTemplate.opsForValue().set(LABEL_BY_ID_CACHE_KEY + id, labelFromDB);
        logger.info("Adding label with ID '{}' to Redis cache...",id);
        return new ResponseEntity<>(labelFromDB, HttpStatus.OK);
      }
      logger.info("The label with the ID '{}' does not exist in the database...",id);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (final Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  /**
   * Retrieves a label by ID from the database.
   * @param id - Long value representing the ID of the label to be retrieved
   * @return IcdConditionsLabel object representing the retrieved label
   */
  @Override
  public IcdConditionsLabel getLabel(final Long id) {
    final Optional<IcdConditionsLabel> labelObj = this.icdConditionsLabelRepository.findById(id);
    if (labelObj.isPresent()) {
      return labelObj.get();
    }
    return null;
  }
  /**
   * Deletes a label by ID from the database and Redis cache.
   * @param id - Long value representing the ID of the label to be deleted
   * @return ResponseEntity object with HTTP status code
   */
  @Override
  public ResponseEntity<IcdConditionsLabel> deleteIcdConditionsLabel(final Long id) {
    final Optional<IcdConditionsLabel> labelObj = this.icdConditionsLabelRepository.findById(id);
    if (labelObj.isPresent()) {
      this.icdConditionsLabelRepository.deleteById(id);
      this.redisTemplate.delete(LABEL_BY_ID_CACHE_KEY + id);
      logger.info("IcdConditionsLabel with id " + id + " has been deleted."); // added logger.info
      return new ResponseEntity<>(HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  /**
   * Updates a label by ID in the database and Redis cache.
   * @param id - Long value representing the ID of the label to be updated
   * @param icdConditionsLabel - IcdConditionsLabel object representing the updated label
   * @return ResponseEntity object containing the updated IcdConditionsLabel object and HTTP status code
   */
  @Override
  public ResponseEntity<IcdConditionsLabel> updateIcdConditionsLabel(final Long id, final IcdConditionsLabel icdConditionsLabel) {
    final Optional<IcdConditionsLabel> labelObj = this.icdConditionsLabelRepository.findById(id);
    if (labelObj.isPresent()) {
      final IcdConditionsLabel labelToUpdate = labelObj.get();
      labelToUpdate.setCode(icdConditionsLabel.getCode());
      labelToUpdate.setCodeDescription(icdConditionsLabel.getCodeDescription());
      final IcdConditionsLabel updatedLabel = this.icdConditionsLabelRepository.save(labelToUpdate);
      this.redisTemplate.opsForValue().set(LABEL_BY_ID_CACHE_KEY + id, updatedLabel);
      logger.info("IcdConditionsLabel with id " + id + " has been updated."); // added logger.info
      return new ResponseEntity<>(updatedLabel, HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }
}
