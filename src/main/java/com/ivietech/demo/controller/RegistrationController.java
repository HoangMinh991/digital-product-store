/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.controller;

import com.ivietech.demo.dto.UserRegistrationDto;
import com.ivietech.demo.entity.User;
import com.ivietech.demo.entity.VerificationToken;
import com.ivietech.demo.event.RegistrationCompleteEvent;
import com.ivietech.demo.service.IUserService;
import com.ivietech.demo.validation.RegValidator;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author minhh
 */
@Controller
public class RegistrationController {

    @Autowired
    IUserService userService;
    @Autowired
    private MessageSource messages;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    RegValidator regValidator;

    @GetMapping("/register")
    public String register(Model model) {
        UserRegistrationDto userFrom = new UserRegistrationDto();
        model.addAttribute("userForm", userFrom);
        return "register";
    }

    @PostMapping("register")
    public String registerUserAccount(@ModelAttribute("userForm") UserRegistrationDto userRegistrationDto,
            BindingResult bindingResult, HttpServletRequest request) {
        regValidator.validate(userRegistrationDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return "register";
        }
        User registered = new User();
        try {
            registered = userService.registerNewUserAccount(userRegistrationDto);
            String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            eventPublisher.publishEvent(new RegistrationCompleteEvent(appUrl, registered, this));

        } catch (RuntimeException ex) {
            return ("register");
        }
        return "redirect:/";
    }
    @GetMapping("/registrationConfirm")
    public String confirmRegistration( Model model, @RequestParam("token") String token) {
        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            model.addAttribute("message", "auth.message.invalidToken");
            return "/user/message";
        }
        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            model.addAttribute("message", "auth.message.expired");
            return "/user/message";
        }
        user.setEnabled(true);
        userService.saveRegisteredUser(user);
        model.addAttribute("message", "Kích hoạt thành công");
        return "/user/message";
    }

}
