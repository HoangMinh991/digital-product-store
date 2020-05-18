package com.ivietech.demo.listener;

import com.ivietech.demo.entity.User;
import com.ivietech.demo.event.RegistrationCompleteEvent;
import com.ivietech.demo.service.IUserService;
import com.ivietech.demo.utils.FormEmail;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class RegistrationListener {

    @Autowired
    private IUserService userService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private FormEmail formEmail;

 
    @EventListener
    public void confirmRegistration(RegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);
        String recipientAddress = user.getEmail();
        String subject = "Xác nhận email đăng kí";
        String confirmationUrl = event.getAppUrl() + "/registrationConfirm?token=" + token;
        String body = "Bạn nhận được email này vì đã đăng kí tài khoản trên website"
                + " \r\n" + "Thông tin tài khoản:"
                + " \r\n" + "User name: " + event.getUser().getUserName()
                +" \r\n" +confirmationUrl;

        SimpleMailMessage email = formEmail.constructEmail(subject, body, recipientAddress);
        mailSender.send(email);
    }

}
