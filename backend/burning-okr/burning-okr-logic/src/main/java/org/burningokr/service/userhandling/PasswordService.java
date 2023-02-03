package org.burningokr.service.userhandling;

import org.burningokr.model.configuration.ConfigurationName;
import org.burningokr.model.mail.Mail;
import org.burningokr.model.users.ChangePasswordData;
import org.burningokr.model.users.LocalUser;
import org.burningokr.model.users.PasswordToken;
import org.burningokr.repositories.users.LocalUserRepository;
import org.burningokr.repositories.users.PasswordTokenRepository;
import org.burningokr.service.configuration.ConfigurationService;
import org.burningokr.service.exceptions.SendingMailFailedException;
import org.burningokr.service.mail.MailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedClientException;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PasswordService {

  private MailService mailService;
  private ConfigurationService configurationService;
  private PasswordTokenRepository passwordTokenRepository;
  private LocalUserRepository localUserRepository;
  private final PasswordEncoder passwordEncoder;

  /**
   * Initializes PasswordService.
   *
   * @param mailService a {@link MailService} object
   * @param configurationService a {@link ConfigurationService} object
   * @param passwordTokenRepository a {@link PasswordTokenRepository} object
   * @param localUserRepository a {@link LocalUserRepository} object
   * @param passwordEncoder a {@link PasswordEncoder} object
   */
  public PasswordService(
      MailService mailService,
      ConfigurationService configurationService,
      PasswordTokenRepository passwordTokenRepository,
      LocalUserRepository localUserRepository,
      PasswordEncoder passwordEncoder) {

    this.mailService = mailService;
    this.configurationService = configurationService;
    this.passwordTokenRepository = passwordTokenRepository;
    this.localUserRepository = localUserRepository;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * Sends the Password Link to new User.
   *
   * @param localUser a {@link LocalUser} object
   */
  public void sendPasswordLinkToNewUser(LocalUser localUser) {
    Mail mail = createMail(localUser);

    mail.setTemplateName("set-password");
    mail.setFrom(getConfigurationValue(ConfigurationName.EMAIL_FROM));
    mail.setSubject(getConfigurationValue(ConfigurationName.EMAIL_SUBJECT_NEW_USER));

    sendPasswordLinkMail(localUser, mail);
  }

  /**
   * Sends a password link to a user.
   *
   * @param user a {@link LocalUser} object
   */
  public void sendPasswordLinkToUser(LocalUser user) {
    Mail mail = createMail(user);

    mail.setTemplateName("reset-password");
    mail.setFrom(getConfigurationValue(ConfigurationName.EMAIL_FROM));
    mail.setSubject(getConfigurationValue(ConfigurationName.EMAIL_SUBJECT_FORGOT_PASSWORD));

    sendPasswordLinkMail(user, mail);
  }

  /**
   * Sets the Password of the User by an email identifier.
   *
   * @param emailIdentifier an {@link UUID} object
   * @param password a string value
   */
  public void setPassword(UUID emailIdentifier, String password) {
    Optional<PasswordToken> opt = passwordTokenRepository.findByEmailIdentifier(emailIdentifier);
    if (opt.isPresent()) {
      PasswordToken passwordToken = opt.get();
      LocalUser localUser = localUserRepository.findByIdOrThrow(passwordToken.getLocalUserId());
      setPassword(localUser, password);
    } else {
      throw new EntityNotFoundException(
          "Entity with emailIdentifier " + emailIdentifier.toString() + " could not be found");
    }
  }

  /**
   * Sets the Password of the User.
   *
   * @param user a {@link LocalUser} object
   * @param password a string value
   */
  public void setPassword(LocalUser user, String password) {
    user.setPassword(encryptPassword(password));
    localUserRepository.save(user);
  }

  /**
   * Changes the Password of the user.
   *
   * @param changePasswordData a {@link ChangePasswordData} object
   */
  public void changePassword(ChangePasswordData changePasswordData) {
    Optional<LocalUser> opt = localUserRepository.findByMail(changePasswordData.getEmail());
    if (opt.isPresent()) {
      LocalUser localUser = opt.get();
      if (passwordEncoder.matches(changePasswordData.getOldPassword(), localUser.getPassword())) {
        localUser.setPassword(encryptPassword(changePasswordData.getNewPassword()));
        localUserRepository.save(localUser);
      } else {
        throw new UnauthorizedClientException("Wrong Password");
      }
    } else {
      throw new EntityNotFoundException(
          "Entity with email" + changePasswordData.getEmail() + " could not be found");
    }
  }

  private String createVerificationLink(UUID id) {
    StringBuilder result = new StringBuilder();
    String server = getConfigurationValue(ConfigurationName.GENERAL_FRONTEND_BASE_URL);
    if (server.endsWith("/")) {
      server = server.substring(0, server.length() - 1);
    }
    result.append(server);
    result.append("/auth/setpassword/").append(id.toString());
    return result.toString();
  }

  private String encryptPassword(String password) {
    return passwordEncoder.encode(password);
  }

  private void sendPasswordLinkMail(LocalUser localUser, Mail mail) {
    try {
      mailService.sendMail(mail);
    } catch (MessagingException e) {
      throw new SendingMailFailedException("Failed sending mail to " + localUser.getMail());
    }
  }

  private void createAndSavePasswordToken(LocalUser localUser, UUID emailIdentifier) {
    PasswordToken passwordToken = new PasswordToken();
    passwordToken.setEmailIdentifier(emailIdentifier);
    passwordToken.setLocalUserId(localUser.getId());
    passwordToken.setCreatedAt(LocalDateTime.now());
    passwordTokenRepository.save(passwordToken);
  }

  private Mail createMail(LocalUser localUser) {
    Mail mail = new Mail();

    UUID emailIdentifier = UUID.randomUUID();
    createAndSavePasswordToken(localUser, emailIdentifier);

    mail.setVariables(getVariablesMap(localUser, emailIdentifier));
    mail.setTo(Arrays.asList(localUser.getMail()));
    return mail;
  }

  private Map<String, Object> getVariablesMap(LocalUser localUser, UUID emailIdentifier) {
    Map<String, Object> thymeleafVariables = new HashMap<>();
    thymeleafVariables.put("contactPerson", localUser.getFullName());
    thymeleafVariables.put("url", createVerificationLink(emailIdentifier));
    return thymeleafVariables;
  }

  private String getConfigurationValue(ConfigurationName configurationName) {
    return configurationService.getConfigurationByName(configurationName.getName()).getValue();
  }
}
