package org.burningokr.model.dashboard;

import lombok.Data;
import lombok.ToString;
import org.burningokr.model.activity.Trackable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Entity
@Data
public class DashboardCreation implements Trackable<Long> {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  // Default length: 255
  private String title;

  @NotNull
  private UUID creatorId;

  @NotNull
  private Long companyId;

  @ToString.Exclude
  @OneToMany(mappedBy = "dashboardCreation", cascade = CascadeType.REMOVE)
  private Collection<ChartCreationOptions> chartCreationOptions = new ArrayList<>();

  @Override
  public String getName() {
    return title;
  }
}
