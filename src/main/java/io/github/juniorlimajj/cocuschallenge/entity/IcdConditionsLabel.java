package io.github.juniorlimajj.cocuschallenge.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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
@Table(name = "icd_conditions_label", schema = "public")
public class IcdConditionsLabel {
  @Id
  private Long id;
  @Column(name = "icd_10")
  private String code;
  @Column(name = "icd_10_description")
  private String codeDescription;
  @ManyToMany(mappedBy = "icdConditionsLabels", cascade = CascadeType.ALL)
  @Column(name = "icd_conditions_label")
  @JsonIgnore
  private List<Case> cases;
}
