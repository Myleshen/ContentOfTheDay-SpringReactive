package dev.myleshenp.contentnotification.notification.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService  {

    final JavaMailSender mailSender;

    public Mono<Boolean> sendNotification(EmailRequest emailRequest) {
        var mail = new SimpleMailMessage();
        mail.setFrom("contact@myleshenp.dev");
        mail.setTo(emailRequest.to());
        mail.setSubject(emailRequest.subject());
        mail.setText(emailRequest.text());
        try{
            mailSender.send(mail);
            return Mono.just(true);
        } catch (MailException e) {
            log.error(e.getMessage());
            return Mono.just(false);
        }
    }
}
