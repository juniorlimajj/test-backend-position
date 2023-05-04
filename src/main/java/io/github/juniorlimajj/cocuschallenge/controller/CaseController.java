/**
 * This package contains the CaseController class, which is responsible for handling HTTP requests related to Case entities.
 */
package io.github.juniorlimajj.cocuschallenge.controller;

import io.github.juniorlimajj.cocuschallenge.entity.Case;
import io.github.juniorlimajj.cocuschallenge.service.CaseService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CaseController {

  @Autowired
  private CaseService caseService;

  /**
   * Handles GET requests to retrieve all Case entities.
   * @return ResponseEntity containing a List of Case entities and an HTTP status code.
   */
  @GetMapping("/case/get/all")
  public ResponseEntity<List<Case>> getAllCases() {
    return this.caseService.getAllCases();
  }

  /**
   * Handles GET requests to retrieve a specific Case entity by ID.
   * @param id The ID of the Case entity to retrieve.
   * @return ResponseEntity containing a Case entity and an HTTP status code.
   */
  @GetMapping("/case/get/{id}")
  public ResponseEntity<Case> getCaseById(@PathVariable final Long id) {
    return this.caseService.getCaseById(id);
  }

  /**
   * Handles POST requests to create a new Case entity.
   * @param caseToSave The Case entity to create.
   * @return ResponseEntity containing the created Case entity and an HTTP status code.
   */
  @PostMapping("/case/create")
  public ResponseEntity<Case> createNewCase(@RequestBody final Case caseToSave) {
    return this.caseService.addCase(caseToSave);
  }

  /**
   * Handles DELETE requests to delete a specific Case entity by ID.
   * @param id The ID of the Case entity to delete.
   * @return ResponseEntity containing the deleted Case entity and an HTTP status code.
   */
  @DeleteMapping("/case/delete/{id}")
  public ResponseEntity<Case> deleteCase(@PathVariable("id") final Long id) {
    return this.caseService.deleteCase(id);
  }

  /**
   * Handles PATCH requests to update a specific Case entity by ID.
   * @param id The ID of the Case entity to update.
   * @param caseToUpdate The updated Case entity.
   * @return ResponseEntity containing the updated Case entity and an HTTP status code.
   */
  @PatchMapping("/case/update/{id}")
  public ResponseEntity<Case> updateCase(@PathVariable("id") final Long id, @RequestBody final Case caseToUpdate) {
    return this.caseService.updateCase(id, caseToUpdate);
  }
}
