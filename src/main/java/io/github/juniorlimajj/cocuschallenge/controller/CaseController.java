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

  @GetMapping("/case/get/all")
  public ResponseEntity<List<Case>> getAllCases() {
    return this.caseService.getAllCases();
  }

  @PostMapping("/case/create")
  public ResponseEntity<Case> createNewCase(@RequestBody final Case caseToSave) {
    return this.caseService.addCase(caseToSave);
  }

  @DeleteMapping("/case/delete/{id}")
  public ResponseEntity<Case> deleteCase(@PathVariable("id") final Long id) {
    return this.caseService.deleteCase(id);
  }

  @PatchMapping("/case/update/{id}")
  public ResponseEntity<Case> updateCase(@PathVariable("id") final Long id, @RequestBody final Case caseToUpdate) {
    return this.caseService.updateCase(id, caseToUpdate);
  }
}
