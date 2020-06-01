/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.listener;


import com.ivietech.demo.entity.User;
import com.ivietech.demo.event.ResetPasswordEvent;
import com.ivietech.demo.service.IUserService;
import com.ivietech.demo.utils.FormEmail;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 *
 * @author minhh
 */
@Service
public class ResetPasswordListener {

    @Autowired
    private IUserService userService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private FormEmail formEmail;

    @EventListener
    @Async
    public void constructResetPass( ResetPasswordEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);
        final String recipientAddress = user.getEmail();
        final String subject = "Đặt lại mật khẩu";
        final String confirmationUrl = event.getAppUrl() + "/passwordResetConfirm?id="
                +event.getUser().getId()+
                "&token=" + token;
        String body = "Để đặt lại mật khẩu vui lòng xác nhận ở link dưới"
                + " \r\n" + confirmationUrl;

        SimpleMailMessage email = formEmail.constructEmail(subject, body, recipientAddress);
        mailSender.send(email);
    }

    
}
