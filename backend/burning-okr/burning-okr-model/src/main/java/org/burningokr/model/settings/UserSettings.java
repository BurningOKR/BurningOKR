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
import org.burningokr.model.structures.Company;
import org.burningokr.model.structures.Department;

@Entity
@Data
public class UserSettings implements Trackable<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private UUID userId;

  @ManyToOne(cascade = CascadeType.REMOVE)
  private Company defaultCompany;

  @ManyToOne(cascade = CascadeType.REMOVE)
  private Department defaultTeam;

  @Override
  public String getName() {
    return "UserSetting (User ID: " + userId + ")";
  }
}
