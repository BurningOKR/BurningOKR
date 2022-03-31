package org.burningokr.model.dashboard;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
public class DashboardCreation {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String title;

  @ToString.Exclude
  @OneToMany(mappedBy = "dashboardCreation", cascade = CascadeType.REMOVE)
  private Collection<ChartCreationOptions> chartCreationOptions = new ArrayList<>();
}
