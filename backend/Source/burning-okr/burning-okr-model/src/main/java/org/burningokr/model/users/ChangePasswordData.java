package org.burningokr.model.users;

import lombok.Data;

@Data
public class ChangePasswordData {
  private String email;
  private String oldPassword;
  private String newPassword;
}
