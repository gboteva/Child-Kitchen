package bg.softuni.childrenkitchen.service.impl;

import bg.softuni.childrenkitchen.exception.EmailProblemException;
import bg.softuni.childrenkitchen.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private static final String recipientEmail = "childrens.kitchen.pl@gmail.com";

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendEmail(String emailSender, String subject, String content) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        try {
            mimeMessageHelper.setFrom(new InternetAddress(emailSender, "App"));
            mimeMessageHelper.setTo(recipientEmail);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(content);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new EmailProblemException();
        }
    }
}
