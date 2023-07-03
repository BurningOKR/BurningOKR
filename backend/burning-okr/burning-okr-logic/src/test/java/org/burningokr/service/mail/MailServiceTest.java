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
//  @InjectMocks
//  private MailService mailService;
//  @Mock
//  private TemplateEngine templateEngine;
//  @Mock
//  private Mail mail;
//  @Mock
//  private MimeMessage message;
//  @Mock
//  private JavaMailSender javaMailSender;
//  @InjectMocks
//  private Optional<JavaMailSender> optionalJavaMailSender;
//
//  //TODO: Fix Test
//  @Test
//  public void sendMail_shouldSend() throws MessagingException {
//    //    Optional<JavaMailSender> javaMailSender = mock(Optional.class);
//
//    when(optionalJavaMailSender.isPresent()).thenReturn(true);
//    when(optionalJavaMailSender.get().createMimeMessage()).thenReturn(message);
//
//    mailService = new MailService(optionalJavaMailSender, templateEngine);
//    mail = new Mail();
//
//    mailService = new MailService(optionalJavaMailSender, templateEngine);
//    mailService.sendMail(mail);
//
//    verify(optionalJavaMailSender.get()).send(message);
//  }
}
