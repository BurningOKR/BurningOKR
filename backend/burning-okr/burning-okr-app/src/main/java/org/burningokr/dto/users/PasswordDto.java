package org.burningokr.dto.users;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class PasswordDto {
  UUID emailIdentifier;
  String password;
  Date dateEmailSendOut;
}
