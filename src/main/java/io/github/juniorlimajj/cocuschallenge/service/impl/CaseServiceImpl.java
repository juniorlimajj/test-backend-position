package io.github.juniorlimajj.cocuschallenge.service.impl;

import io.github.juniorlimajj.cocuschallenge.entity.Case;
import io.github.juniorlimajj.cocuschallenge.repository.CaseRepository;
import io.github.juniorlimajj.cocuschallenge.repository.IcdConditionsLabelRepository;
import io.github.juniorlimajj.cocuschallenge.service.CaseService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CaseServiceImpl implements CaseService {
  private final CaseRepository caseRepository;

  private final IcdConditionsLabelRepository icdConditionsLabelRepository;
  @Autowired
  public CaseServiceImpl(final CaseRepository caseRepository,
      final IcdConditionsLabelRepository icdConditionsLabelRepository) {
    this.caseRepository = caseRepository;
    this.icdConditionsLabelRepository = icdConditionsLabelRepository;
  }

  @Override
  public ResponseEntity<List<Case>> getAllCases() {
    final List<Case> cases = this.caseRepository.findAll();
    return new ResponseEntity<>(cases, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Case> getCaseById(final Long id) {
    return null;
  }

  @Override
  public ResponseEntity<Case> addCase(final Case caseObj) {
    final Case caseToPersist = this.caseRepository.save(
        Case.builder()
            .id(caseObj.getId())
            .description(caseObj.getDescription())
            .doctor(caseObj.getDoctor())
            .icdConditionsLabels(caseObj.getIcdConditionsLabels())
            .timeToLabel(caseObj.getTimeToLabel())
            .build()
    );
    return new ResponseEntity<>(caseToPersist, HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<Case> updateCase(final Long id, final Case caseObj) {
    final Optional<Case> optionalCase = this.caseRepository.findById(id);
    if (optionalCase.isPresent()) {
      final Case caseToUpdate = optionalCase.get();
      caseToUpdate.setDescription(caseObj.getDescription());
      caseToUpdate.setDoctor(caseObj.getDoctor());
      caseToUpdate.setIcdConditionsLabels(caseObj.getIcdConditionsLabels());
      caseToUpdate.setTimeToLabel(caseObj.getTimeToLabel());
      final Case updatedCase = this.caseRepository.save(caseToUpdate);
      return new ResponseEntity<>(updatedCase, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<Case> deleteCase(final Long id) {
    final Optional<Case> optionalCase = this.caseRepository.findById(id);
    if (optionalCase.isPresent()) {
      this.caseRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}
