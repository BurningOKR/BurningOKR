package org.burningokr.model.dashboard;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.*;
import lombok.Data;

@Entity
@Data
public class ChartCreationOptions {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String title;

  private ChartInformationTypeEnum chartType;

  @ManyToOne private DashboardCreation dashboardCreation;

  @ElementCollection
  @CollectionTable(name = "chart_creation_team")
  @Column(name = "chart_creation_team_id")
  private Collection<Long> teamIds = new ArrayList<>();
}
