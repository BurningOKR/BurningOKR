package org.burningokr.model.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.burningokr.model.activity.Trackable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocalUser implements User, Trackable<UUID> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  private String givenName;
  private String surname;
  private String mail;
  private String jobTitle;
  private String department;
  private String photo;
  private boolean active;
  private LocalDateTime createdAt;
  private String password;

  public String getFullName() {
    return givenName + " " + surname;
  }

  @Override
  public String getName() {
    return String.format("%s%s", givenName.charAt(0), surname.charAt(0));
  }
}
