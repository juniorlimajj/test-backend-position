/**
 * This package contains the LabelController class, which is responsible for handling HTTP requests related to IcdConditionsLabel objects.
 */
package io.github.juniorlimajj.cocuschallenge.controller;

import io.github.juniorlimajj.cocuschallenge.entity.IcdConditionsLabel;
import io.github.juniorlimajj.cocuschallenge.service.IcdConditionsLabelService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

  /**
   * Constructor for the LabelController class.
   * @param icdConditionsLabelService An instance of the IcdConditionsLabelService class.
   */
  @Autowired
  public LabelController(final IcdConditionsLabelService icdConditionsLabelService) {
    this.icdConditionsLabelService = icdConditionsLabelService;
  }

  /**
   * Handles HTTP GET requests to retrieve all IcdConditionsLabel objects.
   * @return A ResponseEntity containing a List of IcdConditionsLabel objects.
   */
  @GetMapping("/get/all/labels")
  public @ResponseBody ResponseEntity<List<IcdConditionsLabel>> getAllIcdConditionsLabels() {
    logger.info("Getting all labels");
    return this.icdConditionsLabelService.getAllLabels();
  }

  /**
   * Handles HTTP GET requests to retrieve a specific IcdConditionsLabel object by ID.
   * @param id The ID of the IcdConditionsLabel object to retrieve.
   * @return A ResponseEntity containing the requested IcdConditionsLabel object.
   */
  @GetMapping("/get/label/{id}")
  public ResponseEntity<IcdConditionsLabel> getLabelById(@PathVariable("id") final long id) {
    logger.info("Getting label by id '{}'", id);
    return this.icdConditionsLabelService.getLabelById(id);
  }

  /**
   * Handles HTTP POST requests to create a new IcdConditionsLabel object.
   * @param icdConditionsLabel The IcdConditionsLabel object to create.
   * @return A ResponseEntity containing the newly created IcdConditionsLabel object.
   */
  @PostMapping("/create/label")
  public ResponseEntity<IcdConditionsLabel> createLabel(@RequestBody final IcdConditionsLabel icdConditionsLabel) {
    logger.info("Creating a new label");
    return this.icdConditionsLabelService.createIcdConditionsLabel(icdConditionsLabel);
  }

  /**
   * Handles HTTP DELETE requests to delete a specific IcdConditionsLabel object by ID.
   * @param id The ID of the IcdConditionsLabel object to delete.
   * @return A ResponseEntity indicating success or failure of the deletion operation.
   */
  @DeleteMapping("/delete/label/{id}")
  public ResponseEntity<Void> deleteLabel(@PathVariable("id") final long id) {
    logger.info("Deleting label with id '{}'", id);
    this.icdConditionsLabelService.deleteIcdConditionsLabel(id);
    return ResponseEntity.ok().build();
  }

  /**
   * Handles HTTP PATCH requests to update a specific IcdConditionsLabel object by ID.
   * @param id The ID of the IcdConditionsLabel object to update.
   * @param icdConditionsLabel The updated IcdConditionsLabel object.
   * @return A ResponseEntity containing the updated IcdConditionsLabel object.
   */
  @PatchMapping("/update/label/{id}")
  public ResponseEntity<IcdConditionsLabel> updateLabel(@PathVariable("id") final long id, @RequestBody final IcdConditionsLabel icdConditionsLabel) {
    logger.info("Updating label with id '{}'", id);
    return this.icdConditionsLabelService.updateIcdConditionsLabel(id, icdConditionsLabel);
  }

}
