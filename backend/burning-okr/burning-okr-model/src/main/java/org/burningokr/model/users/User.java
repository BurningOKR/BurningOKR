package org.burningokr.model.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface User {

  UUID getId();

  void setId(UUID id);

  String getGivenName();

  void setGivenName(String givenName);

  String getSurname();

  void setSurname(String surname);

  String getMail();

  void setMail(String mail);

  String getJobTitle();

  void setJobTitle(String hobTitle);

  String getDepartment();

  void setDepartment(String department);

  String getPhoto();

  void setPhoto(String photo);
}
