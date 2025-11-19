package com.example.backend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void sendVerificationEmail_ShouldSendEmail_WhenDataIsValid() throws MessagingException {

        MimeMessage mimeMessage = mock(MimeMessage.class);

        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendVerificationEmail("test@example.com", "Weryfikacja", "Kod: 123");

        verify(emailSender, times(1)).send(mimeMessage);
    }


    @Test
    void sendVerificationEmail_ShouldThrowException_WhenSendingFails() {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);

        doThrow(new RuntimeException("SMTP Error")).when(emailSender).send(mimeMessage);

        assertThrows(RuntimeException.class, () -> {
            emailService.sendVerificationEmail("test@example.com", "Temat", "Treść");
        });
    }

}