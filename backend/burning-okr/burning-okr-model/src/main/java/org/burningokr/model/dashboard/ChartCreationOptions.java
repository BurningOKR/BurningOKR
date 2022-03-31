package org.burningokr.model.dashboard;

import lombok.Data;
import lombok.ToString;
import org.burningokr.model.okrUnits.OkrDepartment;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Entity
@Data
public class ChartCreationOptions {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String title;

  private ChartType chartType;

  @ManyToOne private DashboardCreation dashboardCreation;

  @ElementCollection
  @CollectionTable(name = "chart_creation_teams")
  @Column(name = "chart_creation_teams")
  private Collection<UUID> okrMemberIds = new ArrayList<>();
}
