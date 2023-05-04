package io.github.juniorlimajj.cocuschallenge.service;

import io.github.juniorlimajj.cocuschallenge.entity.Case;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface CaseService {
  ResponseEntity<List<Case>> getAllCases();
  ResponseEntity<Case> getCaseById(Long id);
  ResponseEntity<Case> addCase(Case caseObj);
  ResponseEntity<Case> updateCase(Long id, Case caseObj);
  ResponseEntity<Case> deleteCase(Long id);
  Case getCase(Long id);
}
