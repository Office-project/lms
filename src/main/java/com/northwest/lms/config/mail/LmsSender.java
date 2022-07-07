package com.northwest.lms.config.mail;


import com.northwest.lms.dtos.EmailSenderDto;

public interface LmsSender {
    void send(EmailSenderDto dto);
}
