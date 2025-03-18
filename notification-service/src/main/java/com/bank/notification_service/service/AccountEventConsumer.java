package com.bank.notification_service.service;

import com.bank.notification_service.AccountEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class AccountEventConsumer {

    @Autowired
    private JavaMailSender mailSender;

    @KafkaListener(topics = "account-topic", groupId = "group_id")
    public void accountCreation(AccountEvent accountEvent) {
        sendEmail(accountEvent.getEmail(), accountEvent.getAccountNumber());
    }

    private void sendEmail(String toEmail, String accountNumber) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Account Created Successfully");
        message.setText("Dear User, your account with number " + accountNumber + " has been created successfully.");  // Email body
        message.setFrom("karanja99erick@gmail.com");

        mailSender.send(message);

        System.out.println("Email sent successfully to " + toEmail);
    }
}
