package io.github.juniorlimajj.cocuschallenge.repository;

import io.github.juniorlimajj.cocuschallenge.entity.IcdConditionsLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IcdConditionsLabelRepository extends JpaRepository<IcdConditionsLabel, Long> {
}
