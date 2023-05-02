package io.github.juniorlimajj.cocuschallenge.service;

import io.github.juniorlimajj.cocuschallenge.entity.IcdConditionsLabel;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface IcdConditionsLabelService {
  ResponseEntity<List<IcdConditionsLabel>> getAllLabels();
  ResponseEntity<IcdConditionsLabel> createIcdConditionsLabel(final IcdConditionsLabel icdConditionsLabel);
  ResponseEntity<IcdConditionsLabel> getLabelById(final Long id);
  IcdConditionsLabel getLabel(final Long id);
  ResponseEntity<IcdConditionsLabel> deleteIcdConditionsLabel(final Long id);
  ResponseEntity<IcdConditionsLabel> updateIcdConditionsLabel(final Long id, final IcdConditionsLabel icdConditionsLabel);
}
