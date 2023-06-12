package org.burningokr.model.dashboard;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import org.burningokr.model.activity.Trackable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Entity
@Data
public class DashboardCreation implements Trackable<Long> {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "hibernate_sequence_generator")
  @SequenceGenerator(name = "hibernate_sequence_generator", sequenceName = "hibernate_sequence", allocationSize = 1)
  private Long id;

  // Default length: 255
  private String title;

  @NotNull
  private UUID creatorId;

  @NotNull
  private Long companyId;

  @ToString.Exclude
  @JoinColumn(name = "dashboard_creation_id", nullable = false)
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  private Collection<ChartCreationOptions> chartCreationOptions = new ArrayList<>();

  @Override
  public String getName() {
    return title;
  }
}
