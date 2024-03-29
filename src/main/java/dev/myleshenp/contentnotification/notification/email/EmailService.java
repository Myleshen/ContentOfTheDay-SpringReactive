package dev.myleshenp.contentnotification.notification.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    final JavaMailSender mailSender;
    final EmailRequestRepository repository;

    public Mono<EmailRequest> sendNotification(Record request) {
        var emailRequest = (EmailRequest) request;
        var mail = mailSender.createMimeMessage();
        try {
            mail.setFrom(new InternetAddress("mylesuge@gmail.com"));
            mail.setRecipients(MimeMessage.RecipientType.TO, emailRequest.to());
            mail.setSubject(emailRequest.subject());
            mail.setContent(emailRequest.text(), "text/html; charset=utf-8");
            mailSender.send(mail);
            return Mono.just(emailRequest);
        } catch (MailException | MessagingException e) {
            log.error(e.getMessage());
            return Mono.error(e);
        }
    }

    @Async
    @Scheduled(cron = "* * 9 * * *")
    public void runAsScheduled() {}
}
