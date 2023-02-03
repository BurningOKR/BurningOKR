package org.burningokr.model.settings;

import lombok.Data;
import org.burningokr.model.activity.Trackable;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
public class UserSettings implements Trackable<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
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
