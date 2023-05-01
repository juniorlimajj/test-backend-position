package io.github.juniorlimajj.cocuschallenge.controller;

import io.github.juniorlimajj.cocuschallenge.entity.IcdConditionsLabel;
import io.github.juniorlimajj.cocuschallenge.service.IcdConditionsLabelService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LabelController {

  private static final Logger logger = LoggerFactory.getLogger(LabelController.class);

  private final IcdConditionsLabelService icdConditionsLabelService;

  @Autowired
  public LabelController(final IcdConditionsLabelService icdConditionsLabelService) {
    this.icdConditionsLabelService = icdConditionsLabelService;
  }

  @GetMapping("/get/all/labels")
  public @ResponseBody ResponseEntity<List<IcdConditionsLabel>> getAllIcdConditionsLabels() {
    logger.info("Getting all labels");
    return this.icdConditionsLabelService.getAllLabels();
  }

  @GetMapping("/get/label/{id}")
  public ResponseEntity<IcdConditionsLabel> getLabelById(@PathVariable("id") final long id) {
    logger.info("Getting label by id '{}'", id);
    return this.icdConditionsLabelService.getLabelById(id);
  }

  @PostMapping("/create/label")
  public ResponseEntity<IcdConditionsLabel> createLabel(@RequestBody final IcdConditionsLabel icdConditionsLabel) {
    logger.info("Creating a new label");
    return this.icdConditionsLabelService.createIcdConditionsLabel(icdConditionsLabel);
  }

  
}
