package org.burningokr.model;

import org.burningokr.model.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
public class UserTest {
  User user;
  UUID userId;
  String surname;
  String givenName;
  String mail;
  String jobTitle;
  String department;
  String photo;
  Boolean active;
  Boolean admin;
  LocalDateTime localDateTime;

  @BeforeEach
  public void setUp() {
    userId = UUID.randomUUID();
    surname = "Mustermann";
    givenName = "Max";
    mail = "user@user.de";
    jobTitle = "Job Title";
    department = "Department";
    photo = "Photo";
    active = true;
    admin = false;
    localDateTime = LocalDateTime.now();

    user = new User();
    user.setId(userId);
    user.setSurname(surname);
    user.setGivenName(givenName);
    user.setMail(mail);
    user.setJobTitle(jobTitle);
    user.setDepartment(department);
    user.setPhoto(photo);
    user.setActive(true);
    user.setAdmin(false);
    user.setCreatedAt(localDateTime);
  }

  @Test
  public void buildUser_shouldCreateAUserUsingBuilder() {
    User buildUser = User.builder()
      .id(userId)
      .surname(surname)
      .givenName(givenName)
      .mail(mail)
      .jobTitle(jobTitle)
      .department(department)
      .photo(photo)
      .active(active)
      .admin(admin)
      .createdAt(localDateTime)
      .build();

    assertNotNull(buildUser);
    assertEquals(userId, buildUser.getId());
    assertEquals(surname, buildUser.getSurname());
    assertEquals(givenName, buildUser.getGivenName());
    assertEquals(mail, buildUser.getMail());
    assertEquals(jobTitle, buildUser.getJobTitle());
    assertEquals(department, buildUser.getDepartment());
    assertEquals(photo, buildUser.getPhoto());
    assertEquals(active, buildUser.isActive());
    assertEquals(admin, buildUser.isAdmin());
    assertEquals(localDateTime, buildUser.getCreatedAt());

  }

  @Test
  public void getUserId_shouldReturnUserId() {
    assertEquals(userId, user.getId());
  }

  @Test
  public void getSurname_shouldReturnSurname() {
    assertEquals(surname, user.getSurname());
  }

  @Test
  public void getGivenName_shouldReturnGivenName() {
    assertEquals(givenName, user.getGivenName());
  }

  @Test
  public void getMail_shouldReturnMail() {
    assertEquals(mail, user.getMail());
  }

  @Test
  public void getJobTitle_shouldReturnJobTitle() {
    assertEquals(jobTitle, user.getJobTitle());
  }

  @Test
  public void getDepartment_shouldReturnDepartment() {
    assertEquals(department, user.getDepartment());
  }

  @Test
  public void getPhoto_shouldReturnPhoto() {
    assertEquals(photo, user.getPhoto());
  }

  @Test
  public void isActive_shouldReturnActiveState() {
    assertEquals(active, user.isActive());
  }

  @Test
  public void isAdmin_shouldReturnAdminState() {
    assertEquals(admin, user.isAdmin());
  }

  @Test
  public void getLocalDateTime_shouldReturnSurname() {
    assertEquals(localDateTime, user.getCreatedAt());
  }
}
