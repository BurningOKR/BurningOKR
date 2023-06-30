package org.burningokr.service.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.burningokr.model.mail.Mail;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class MailServiceTest {
  @InjectMocks
  private MailService mailService;
  @Mock
  private TemplateEngine templateEngine;
  @Mock
  private Mail mail;
  @Mock
  private MimeMessage message;

  @Test
  public void sendMail_shouldSend() throws MessagingException {
    Optional<JavaMailSender> javaMailSender = mock(Optional.class);
    mailService = new MailService(javaMailSender, templateEngine);
    mail = new Mail();

    when(mailService.hasMailConfigured()).thenReturn(true);
    when(javaMailSender.get().createMimeMessage()).thenReturn(message);

    mailService = new MailService(javaMailSender, templateEngine);
    mailService.sendMail(mail);
    verify(javaMailSender, times(1)).get().send(message);
  }
}
