package com.example.demo.service;


import com.example.demo.dto.SubscriberDTO;
import com.example.demo.model.Subscriber;
import com.example.demo.repositories.SubscriberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;

@Service
public class SubscriberServiceImpl implements SubscriberService {


    @Autowired
    private SubscriberRepository subscriberRepository;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;


    public Subscriber saveSubscriber(SubscriberDTO subscriberDTO) {
        // check if email already exists
        Optional<Subscriber> existingSubscriber = subscriberRepository.findById(subscriberDTO.getEmail());
        if (existingSubscriber.isPresent()) {
            throw new EmailAlreadyExistsException("Эл. адрес уже существует");
        }

        // generate unique promo code
        String promoCode;
        do {
            promoCode = generatePromoCode();
        } while (subscriberRepository.findByPromoCode(promoCode).isPresent());

        // save to database
        Subscriber subscriber = new Subscriber();
        subscriber.setEmail(subscriberDTO.getEmail());
        subscriber.setFirstName(subscriberDTO.getFirstName());
        subscriber.setLastName(subscriberDTO.getLastName());
        subscriber.setPromoCode(promoCode);
        subscriberRepository.save(subscriber);

        // send email
        try {
            sendEmail(subscriberDTO.getEmail(), "Congratulations!", "Your unique promo code is: " + promoCode);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }

        return subscriber;
    }

    public static class EmailAlreadyExistsException extends RuntimeException {
        public EmailAlreadyExistsException(String message) {
            super(message);
        }
    }

    private void sendEmail(String to, String subject, String text) throws MessagingException {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(text);

        Transport.send(message);
    }
    public List<Subscriber> findAllSubscribers() {
        return subscriberRepository.findAll();
    }

    private String generatePromoCode() {
        // generate a random 4-character string consisting of uppercase letters and digits
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder promoCode = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 4; i++) {
            promoCode.append(characters.charAt(rnd.nextInt(characters.length())));
        }
        return promoCode.toString();
    }
    @Override
    public void deleteSubscriberByEmail(String email) {
        subscriberRepository.deleteById(email);
    }

    @Override
    public Optional<Subscriber> findSubscriberByEmail(String email) {
        return subscriberRepository.findById(email);
    }
    @Scheduled(cron = "0 0 0 1 * ?")  // runs at 00:00:00 on the 1st day of every month
    public void sendMonthlyEmails() {
        List<Subscriber> subscribers = subscriberRepository.findAll();
        for (Subscriber subscriber : subscribers) {
            String promoCode = generatePromoCode();
            subscriber.setPromoCode(promoCode);
            subscriberRepository.save(subscriber);

            try {
                sendEmail(subscriber.getEmail(), "Your new promo code", "Your promo code for this month is: " + promoCode);
            } catch (MessagingException e) {
                throw new RuntimeException("Failed to send email", e);
            }
        }
    }

}
