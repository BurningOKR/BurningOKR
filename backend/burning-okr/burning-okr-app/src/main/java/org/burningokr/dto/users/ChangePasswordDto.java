package org.burningokr.dto.users;

import lombok.Data;

@Data
public class ChangePasswordDto {
  String newPassword;
  String oldPassword;
  String email;
}
