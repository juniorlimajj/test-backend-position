package io.github.juniorlimajj.cocuschallenge.service;

import io.github.juniorlimajj.cocuschallenge.entity.IcdConditionsLabel;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface IcdConditionsLabelService {
  ResponseEntity<List<IcdConditionsLabel>> getAllLabels();
}
