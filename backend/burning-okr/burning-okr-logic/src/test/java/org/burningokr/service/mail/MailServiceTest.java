package org.burningokr.service.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.burningokr.model.mail.Mail;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MailServiceTest {
  private final TemplateEngine templateEngine = mock(TemplateEngine.class);

  @Test
  public void sendMail_shouldSend() throws MessagingException {
    JavaMailSender mailSender = mock(JavaMailSender.class);
    Optional<JavaMailSender> optionalJavaMailSender = Optional.of(mailSender);
    MailService mailService = new MailService(optionalJavaMailSender, templateEngine);
    when(templateEngine.process((String) any(), any())).thenReturn("wurscht");
    List<String> recipients = new ArrayList<>();
    recipients.add("target@brockhaus-ag.de");

    MimeMessage message = mock(MimeMessage.class);
    Mail mail = new Mail();
    mail.setTemplateName("wurscht.html");
    mail.setSubject("Meine Nachricht");
    mail.setFrom("from@brockhaus-ag.de");
    mail.setTo(recipients);
    when(mailSender.createMimeMessage()).thenReturn(message);

    mailService.sendMail(mail);

    verify(optionalJavaMailSender.get()).send(message);
  }

  @Test
  public void sendMail_shouldBeAbleToSendWithTemplateWithoutHtml() throws MessagingException {
    JavaMailSender mailSender = mock(JavaMailSender.class);
    Optional<JavaMailSender> optionalJavaMailSender = Optional.of(mailSender);
    MailService mailService = new MailService(optionalJavaMailSender, templateEngine);
    when(templateEngine.process((String) any(), any())).thenReturn("wurscht");
    List<String> recipients = new ArrayList<>();
    recipients.add("target@brockhaus-ag.de");

    MimeMessage message = mock(MimeMessage.class);
    Mail mail = new Mail();
    mail.setTemplateName("wurscht");
    mail.setSubject("Meine Nachricht");
    mail.setFrom("from@brockhaus-ag.de");
    mail.setTo(recipients);
    when(mailSender.createMimeMessage()).thenReturn(message);

    mailService.sendMail(mail);

    verify(optionalJavaMailSender.get()).send(message);
  }

  @Test
  public void sendMail_shouldBeAbleToHandleNull() {
    JavaMailSender mailSender = mock(JavaMailSender.class);
    Optional<JavaMailSender> optionalJavaMailSender = Optional.of(mailSender);
    MailService mailService = new MailService(optionalJavaMailSender, templateEngine);
    when(templateEngine.process((String) any(), any())).thenReturn("wurscht");
    MimeMessage message = mock(MimeMessage.class);
    when(mailSender.createMimeMessage()).thenReturn(message);

    assertThrows(NullPointerException.class, () -> mailService.sendMail(null));
  }

  @Test
  public void sendMail_shouldNotSendBecauseMailSenderEmpty() throws MessagingException {
    JavaMailSender mailSender = mock(JavaMailSender.class);
    Optional optionalJavaMailSender = mock(Optional.class);
    MailService mailService = new MailService(optionalJavaMailSender, templateEngine);

    MimeMessage message = mock(MimeMessage.class);
    Mail mail = new Mail();
    when(mailSender.createMimeMessage()).thenReturn(message);
    when(optionalJavaMailSender.isPresent()).thenReturn(false);

    mailService.sendMail(mail);

    verify(optionalJavaMailSender, never()).get();
  }

  @Test
  public void sendMail_shouldSendIsConfiguredWhenOptionalIsPresent() {
    Optional optionalJavaMailSender = mock(Optional.class);
    MailService mailService = new MailService(optionalJavaMailSender, templateEngine);

    when(optionalJavaMailSender.isPresent()).thenReturn(true);

    boolean result = mailService.hasMailConfigured();

    assertTrue(result);
  }

  @Test
  public void sendMail_shouldSendIsNotConfiguredWhenOptionalIsNotPresent() {
    Optional optionalJavaMailSender = mock(Optional.class);
    MailService mailService = new MailService(optionalJavaMailSender, templateEngine);

    when(optionalJavaMailSender.isPresent()).thenReturn(false);

    boolean result = mailService.hasMailConfigured();

    assertFalse(result);
  }

  @Test
  public void sendMail_shouldAddEmptyToListIfNoAddressGiven() throws MessagingException {
    JavaMailSender mailSender = mock(JavaMailSender.class);
    Optional<JavaMailSender> optionalJavaMailSender = Optional.of(mailSender);
    MailService mailService = new MailService(optionalJavaMailSender, templateEngine);
    when(templateEngine.process((String) any(), any())).thenReturn("wurscht");

    MimeMessage message = mock(MimeMessage.class);
    Mail mail = new Mail();
    mail.setTemplateName("wurscht");
    mail.setSubject("Meine Nachricht");
    mail.setFrom("from@brockhaus-ag.de");
    when(mailSender.createMimeMessage()).thenReturn(message);

    mailService.sendMail(mail);

    verify(optionalJavaMailSender.get()).send(message);
    assertNotNull(mail.getTo());
    assertEquals(0, mail.getTo().size());
  }

  @Test
  public void sendMail_shouldAddEmptyBccListIfNoAddressGiven() throws MessagingException {
    JavaMailSender mailSender = mock(JavaMailSender.class);
    Optional<JavaMailSender> optionalJavaMailSender = Optional.of(mailSender);
    MailService mailService = new MailService(optionalJavaMailSender, templateEngine);
    when(templateEngine.process((String) any(), any())).thenReturn("wurscht");

    MimeMessage message = mock(MimeMessage.class);
    Mail mail = new Mail();
    mail.setTemplateName("wurscht");
    mail.setSubject("Meine Nachricht");
    mail.setFrom("from@brockhaus-ag.de");
    when(mailSender.createMimeMessage()).thenReturn(message);

    mailService.sendMail(mail);

    verify(optionalJavaMailSender.get()).send(message);
    assertNotNull(mail.getBcc());
    assertEquals(0, mail.getBcc().size());
  }

  @Test
  public void sendMail_shouldAddEmptyCCListIfNoAddressGiven() throws MessagingException {
    JavaMailSender mailSender = mock(JavaMailSender.class);
    Optional<JavaMailSender> optionalJavaMailSender = Optional.of(mailSender);
    MailService mailService = new MailService(optionalJavaMailSender, templateEngine);
    when(templateEngine.process((String) any(), any())).thenReturn("wurscht");

    MimeMessage message = mock(MimeMessage.class);
    Mail mail = new Mail();
    mail.setTemplateName("wurscht");
    mail.setSubject("Meine Nachricht");
    mail.setFrom("from@brockhaus-ag.de");
    when(mailSender.createMimeMessage()).thenReturn(message);

    mailService.sendMail(mail);

    verify(optionalJavaMailSender.get()).send(message);
    assertNotNull(mail.getCc());
    assertEquals(0, mail.getCc().size());
  }

  @Test
  public void sendMail_shouldAddEmptyVariablesListIfNoAddressGiven() throws MessagingException {
    JavaMailSender mailSender = mock(JavaMailSender.class);
    Optional<JavaMailSender> optionalJavaMailSender = Optional.of(mailSender);
    MailService mailService = new MailService(optionalJavaMailSender, templateEngine);
    when(templateEngine.process((String) any(), any())).thenReturn("wurscht");

    MimeMessage message = mock(MimeMessage.class);
    Mail mail = new Mail();
    mail.setTemplateName("wurscht");
    mail.setSubject("Meine Nachricht");
    mail.setFrom("from@brockhaus-ag.de");
    when(mailSender.createMimeMessage()).thenReturn(message);

    mailService.sendMail(mail);

    verify(optionalJavaMailSender.get()).send(message);
    assertNotNull(mail.getVariables());
    assertEquals(0, mail.getVariables().size());
  }
}
