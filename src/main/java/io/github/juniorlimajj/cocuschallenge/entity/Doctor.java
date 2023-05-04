package io.github.juniorlimajj.cocuschallenge.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "doctor", schema = "public")
public class Doctor {
  @Id
  private Long id;
  @Column(name = "name")
  private String name;
  @OneToMany(mappedBy = "doctor")
  @JsonIgnore
  private List<Case> cases;
}
