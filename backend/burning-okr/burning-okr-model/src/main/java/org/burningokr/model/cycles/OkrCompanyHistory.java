package org.burningokr.model.cycles;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Data;
import org.burningokr.model.activity.Trackable;
import org.burningokr.model.okrUnits.OkrCompany;

@Entity
@Data
public class OkrCompanyHistory implements Trackable<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @OneToMany(mappedBy = "history", cascade = CascadeType.REMOVE)
  private Collection<OkrCompany> companies = new ArrayList<>();

  @Override
  public String getName() {
    return "History " + id;
  }
}
