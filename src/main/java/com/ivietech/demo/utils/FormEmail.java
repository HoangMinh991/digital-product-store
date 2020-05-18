/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.utils;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

/**
 *
 * @author minhh
 */
@Component
public class FormEmail {
    public SimpleMailMessage constructEmail(String subject, String body,
            String recipientAddress) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(recipientAddress);
        email.setFrom("support.email");
        return email;
    }
}
