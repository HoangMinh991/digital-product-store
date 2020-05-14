/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.controller;

import com.ivietech.demo.dto.UserDto;
import com.ivietech.demo.entity.User;
import com.ivietech.demo.service.IUserService;
import com.ivietech.demo.validation.RegValidator;
import static com.sun.corba.se.spi.presentation.rmi.StubAdapter.request;
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
        UserDto userFrom = new UserDto();
        model.addAttribute("userForm", userFrom);
        return "user/register";
    }

    @PostMapping("/register")
    public String registerUserAccount(@ModelAttribute("userForm") UserDto userDto,
            BindingResult bindingResult, HttpServletRequest request) {
        regValidator.validate(userDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return "user/register";
        }
        User registered = new User();
        registered = userService.registerNewUserAccount(userDto);
        return "redirect:/";
    }

}
