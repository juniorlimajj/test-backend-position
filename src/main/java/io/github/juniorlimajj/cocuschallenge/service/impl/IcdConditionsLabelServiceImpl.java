package io.github.juniorlimajj.cocuschallenge.service.impl;

import io.github.juniorlimajj.cocuschallenge.entity.IcdConditionsLabel;
import io.github.juniorlimajj.cocuschallenge.repository.IcdConditionsLabelRepository;
import io.github.juniorlimajj.cocuschallenge.service.IcdConditionsLabelService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class IcdConditionsLabelServiceImpl implements IcdConditionsLabelService {

  private final IcdConditionsLabelRepository icdConditionsLabelRepository;
  private static final Logger logger = LoggerFactory.getLogger(IcdConditionsLabelServiceImpl.class);

  @Autowired
  public IcdConditionsLabelServiceImpl(IcdConditionsLabelRepository icdConditionsLabelRepository) {
    this.icdConditionsLabelRepository = icdConditionsLabelRepository;
  }

  @Override
  public ResponseEntity<List<IcdConditionsLabel>> getAllLabels() {
    try {
      logger.info("Retrieving all labels");
      return new ResponseEntity<>(icdConditionsLabelRepository.findAll(),HttpStatus.OK);
    } catch (Exception e) {
      logger.error("Error while getting all labels", e);
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}