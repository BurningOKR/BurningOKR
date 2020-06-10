package org.burningokr.model.settings;

import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Data;
import org.burningokr.model.activity.Trackable;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;

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
