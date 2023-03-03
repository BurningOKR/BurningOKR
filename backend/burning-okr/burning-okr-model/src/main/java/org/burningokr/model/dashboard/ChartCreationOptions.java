package org.burningokr.model.dashboard;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
public class ChartCreationOptions {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String title;

  private ChartInformationTypeEnum chartType;

  @ManyToOne
  private DashboardCreation dashboardCreation;

  @ElementCollection
  @CollectionTable(name = "chart_creation_team")
  @Column(name = "chart_creation_team_id")
  private Collection<Long> teamIds = new ArrayList<>();
}
