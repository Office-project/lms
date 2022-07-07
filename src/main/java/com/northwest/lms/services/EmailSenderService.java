package com.northwest.lms.services;



import com.northwest.lms.dtos.EmailSenderDto;
import org.springframework.http.ResponseEntity;

import javax.mail.MessagingException;
import java.net.MalformedURLException;

public interface EmailSenderService {
    ResponseEntity<String> send(EmailSenderDto emailSenderDto) throws MessagingException;
    void sendRegistrationEmail(String email, String token) throws MalformedURLException;
}
