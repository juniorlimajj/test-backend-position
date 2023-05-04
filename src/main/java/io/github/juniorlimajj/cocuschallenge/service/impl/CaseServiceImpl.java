/**
 * This class implements the CaseService interface and provides methods to interact with the Case entity.
 * It uses Redis cache to store and retrieve data for faster access.
 */

package io.github.juniorlimajj.cocuschallenge.service.impl;

import io.github.juniorlimajj.cocuschallenge.entity.Case;
import io.github.juniorlimajj.cocuschallenge.repository.CaseRepository;
import io.github.juniorlimajj.cocuschallenge.repository.IcdConditionsLabelRepository;
import io.github.juniorlimajj.cocuschallenge.service.CaseService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CaseServiceImpl implements CaseService {
  //TODO
  //Retrieve cases filter by label
  //Delete old label in the case
  private final CaseRepository caseRepository;

  private final RedisTemplate<String, Object> redisTemplate;

  private static final Logger logger = LoggerFactory.getLogger(CaseServiceImpl.class);

  private final IcdConditionsLabelRepository icdConditionsLabelRepository;

  private static final String ALL_CASES_CACHE_KEY = "allCases";

  private static final String CASE_BY_ID_CACHE_KEY = "case_";

  static final long TTL_SECONDS = 3600;

  /**
   * Constructor to initialize the CaseServiceImpl instance with the required dependencies.
   * @param caseRepository The CaseRepository instance to interact with the database.
   * @param redisTemplate The RedisTemplate instance to interact with Redis cache.
   * @param icdConditionsLabelRepository The IcdConditionsLabelRepository instance to interact with the IcdConditionsLabel entity.
   */
  @Autowired
  public CaseServiceImpl(final CaseRepository caseRepository,
      final RedisTemplate<String, Object> redisTemplate, final IcdConditionsLabelRepository icdConditionsLabelRepository) {
    this.caseRepository = caseRepository;
    this.redisTemplate = redisTemplate;
    this.icdConditionsLabelRepository = icdConditionsLabelRepository;
  }

  /**
   * Retrieves all cases from Redis cache or database.
   * @return A ResponseEntity containing a list of Case objects and an HTTP status code.
   */
  @Override
  public ResponseEntity<List<Case>> getAllCases() {
    try {
      logger.info("Retrieving all cases from Redis cache with key '{}'", ALL_CASES_CACHE_KEY);
      List<Case> cases = (List<Case>) this.redisTemplate.opsForValue().get(ALL_CASES_CACHE_KEY);
      if (cases == null) {
        logger.info("Cache miss. Retrieving all cases from database");
        cases = this.caseRepository.findAll();
        this.redisTemplate.opsForValue().set(ALL_CASES_CACHE_KEY, cases);
        logger.info("Stored all cases in Redis cache with key '{}' and TTL of {} seconds", ALL_CASES_CACHE_KEY, TTL_SECONDS);
      } else {
        logger.info("Retrieved all cases from Redis cache with key '{}'", ALL_CASES_CACHE_KEY);
      }
      return new ResponseEntity<>(cases, HttpStatus.OK);
    } catch (final Exception e) {
      logger.error("Error while getting all cases", e);
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Retrieves a case by its ID from Redis cache or database.
   * @param id The ID of the case to retrieve.
   * @return A ResponseEntity containing a Case object and an HTTP status code.
   */
  @Override
  public ResponseEntity<Case> getCaseById(final Long id) {
    try {
      final Case caseResultSet = (Case) this.redisTemplate.opsForValue().get(CASE_BY_ID_CACHE_KEY + id);
      if (caseResultSet != null){
        logger.info("Getting case with ID '{}' from Redis cache...",id);
        return new ResponseEntity<>(caseResultSet, HttpStatus.OK);
      }
      logger.info("The case with the ID '{}' does not exist in Redis cache...",id);
      final Case caseFromDB = this.getCase(id);
      if (caseFromDB != null) {
        this.redisTemplate.opsForValue().set(CASE_BY_ID_CACHE_KEY + id, caseFromDB);
        logger.info("Adding case with ID '{}' to Redis cache...",id);
        return new ResponseEntity<>(caseFromDB, HttpStatus.OK);
      }
      logger.info("The case with the ID '{}' does not exist in the database...",id);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (final Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Adds a new case to the database.
   * @param caseObj The Case object to add.
   * @return A ResponseEntity containing the added Case object and an HTTP status code.
   */
  @Override
  public ResponseEntity<Case> addCase(final Case caseObj) {
    logger.info("Creating a new case with ID '{}' ...", caseObj.getId());
    final Case caseToPersist = this.caseRepository.save(
        Case.builder()
            .id(caseObj.getId())
            .description(caseObj.getDescription())
            .doctor(caseObj.getDoctor())
            .icdConditionsLabels(caseObj.getIcdConditionsLabels())
            .timeToLabel(caseObj.getTimeToLabel())
            .build()
    );
    logger.info("New case successfully created");
    return new ResponseEntity<>(caseToPersist, HttpStatus.CREATED);
  }

  /**
   * Updates an existing case in the database.
   * @param id The ID of the case to update.
   * @param caseObj The Case object with updated values.
   * @return A ResponseEntity containing the updated Case object and an HTTP status code.
   */
  @Override
  public ResponseEntity<Case> updateCase(final Long id, final Case caseObj) {
    logger.info("Updating case with ID '{}'", caseObj.getId());
    final Optional<Case> optionalCase = this.caseRepository.findById(id);
    if (optionalCase.isPresent()) {
      final Case caseToUpdate = optionalCase.get();
      caseToUpdate.setDescription(caseObj.getDescription());
      caseToUpdate.setDoctor(caseObj.getDoctor());
      caseToUpdate.setIcdConditionsLabels(caseObj.getIcdConditionsLabels());
      caseToUpdate.setTimeToLabel(caseObj.getTimeToLabel());
      final Case updatedCase = this.caseRepository.save(caseToUpdate);
      logger.info("Done case with ID '{}' is up to date", caseObj.getId());
      return new ResponseEntity<>(updatedCase, HttpStatus.OK);
    } else {
      logger.info("Error updating case with ID '{}'", caseObj.getId());
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Deletes a case from the database.
   * @param id The ID of the case to delete.
   * @return A ResponseEntity with an HTTP status code.
   */
  @Override
  public ResponseEntity<Case> deleteCase(final Long id) {
    logger.info("Deleting case with ID '{}'", id);
    final Optional<Case> optionalCase = this.caseRepository.findById(id);
    if (optionalCase.isPresent()) {
      this.caseRepository.deleteById(id);
      logger.info("Deleting case with ID '{}'", id);
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      logger.info("Id not '{}' found", id);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Retrieves a case by its ID from the database.
   * @param id The ID of the case to retrieve.
   * @return A Case object or null if not found.
   */
  @Override
  public Case getCase(final Long id) {
    final Optional<Case> caseObj = this.caseRepository.findById(id);
    if (caseObj.isPresent()) {
      return caseObj.get();
    }
    return null;
  }
}
