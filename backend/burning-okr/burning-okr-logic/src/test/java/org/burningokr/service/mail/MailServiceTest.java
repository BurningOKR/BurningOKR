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

import static org.mockito.Mockito.*;

public class MailServiceTest {
  private final TemplateEngine templateEngine = mock(TemplateEngine.class);

  @Test
  public void sendMail_shouldSend() throws MessagingException {
    JavaMailSender mailSender = mock(JavaMailSender.class);
    Optional<JavaMailSender> optionalJavaMailSender = Optional.of(mailSender);
    MailService mailService = new MailService(optionalJavaMailSender, templateEngine);
    when(templateEngine.process((String) any(), any())).thenReturn("wurscht");
    List<String> recipients = new ArrayList<String>();
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
}
