package bg.softuni.childrenkitchen.service;

import jakarta.mail.MessagingException;

public interface EmailService {

    void sendEmail(String emailSender, String subject, String content) throws MessagingException;
}
