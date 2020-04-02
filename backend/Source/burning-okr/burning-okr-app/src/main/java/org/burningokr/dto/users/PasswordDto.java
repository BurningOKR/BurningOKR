package org.burningokr.dto.users;

import java.util.Date;
import java.util.UUID;
import lombok.Data;

@Data
public class PasswordDto {
  UUID emailIdentifier;
  String password;
  Date dateEmailSendOut;
}
