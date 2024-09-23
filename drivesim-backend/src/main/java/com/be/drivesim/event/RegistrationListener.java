package com.be.drivesim.event;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.be.drivesim.model.User;
import com.be.drivesim.service.UserService;

@Component
public class RegistrationListener implements 
  ApplicationListener<OnRegistrationCompleteEvent> {
 
    @Autowired
    private UserService service;
 
    @Autowired
    private MessageSource messages;
 
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        service.createVerificationTokenForUser(user, token);
        
        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl 
          = event.getAppUrl() + "/api/user/registration_confirm?token=" + token;
        String message = "Please verify your email:";
        
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + "http://localhost:8081" + confirmationUrl);
        mailSender.send(email);
    }
}
