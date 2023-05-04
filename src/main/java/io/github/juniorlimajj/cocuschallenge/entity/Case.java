package io.github.juniorlimajj.cocuschallenge.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "case", schema = "public")
public class Case {
  @Id
  private Long id;
  @Column(name = "description")
  private String description;

  @ManyToOne
  @JoinColumn(name = "doctor_id")
  private Doctor doctor;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "case_icd_conditions_label", joinColumns = @JoinColumn(name = "icd_conditions_label_id"),
  inverseJoinColumns = @JoinColumn(name = "case_id"))
  private List<IcdConditionsLabel> icdConditionsLabels;

  @Column(name = "time_to_label")
  private String timeToLabel;
}