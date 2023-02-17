package org.burningokr.model.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AadUser implements User {

  @Id
  private UUID id;
  private String givenName;
  private String surname;
  private String mail;
  private String jobTitle;
  private String department;
  private String mailNickname;
  private String photo;
}
