package org.burningokr.model.settings;

import jakarta.persistence.*;
import lombok.Data;
import org.burningokr.model.activity.Trackable;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;

import java.util.UUID;

@Entity
@Data
public class UserSettings implements Trackable<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "hibernate_sequence_generator")
  @SequenceGenerator(name = "hibernate_sequence_generator", sequenceName = "hibernate_sequence", allocationSize = 1)
  private Long id;

  private UUID userId;

  @ManyToOne(cascade = CascadeType.REMOVE)
  private OkrCompany defaultOkrCompany;

  @ManyToOne(cascade = CascadeType.REMOVE)
  private OkrDepartment defaultTeam;

  @Override
  public String getName() {
    return "UserSetting (User ID: " + userId + ")";
  }
}
