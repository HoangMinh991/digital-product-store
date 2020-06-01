/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.controller;

import com.ivietech.demo.dao.PasswordResetTokenRepository;
import com.ivietech.demo.dao.UserRepository;
import com.ivietech.demo.dto.NewPassDto;
import com.ivietech.demo.dto.UpdatePasswordDto;
import com.ivietech.demo.entity.User;
import com.ivietech.demo.event.ResetPasswordEvent;
import com.ivietech.demo.security.SecurityService;
import com.ivietech.demo.service.IUserService;
import com.ivietech.demo.validation.ChangePasswordValidator;
import com.ivietech.demo.validation.NewPasswordValidator;
import com.ivietech.demo.validation.PasswordResetValidator;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author minhh
 */
@Controller
public class PasswordResetController {

    @Autowired
    private IUserService userService;
    @Autowired
    PasswordResetTokenRepository passwordTokenRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private SecurityService securityService;
    @Autowired
    private PasswordResetValidator passwordResetValidator;

    @Autowired
    private NewPasswordValidator newPasswordValidator;
    @Autowired
    private ChangePasswordValidator changePasswordValidator;

    @GetMapping("/resetPassword")
    @ResponseBody
    public String resetPassword(HttpServletRequest request,
            @RequestParam("email") String userEmail) {

        if (!passwordResetValidator.validate(userEmail)) {
            return "/";
        }
        User user = userRepository.findByEmail(userEmail);
        String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        applicationEventPublisher.publishEvent(new ResetPasswordEvent(user, appUrl));
        return "///";
    }

    @GetMapping("/passwordResetConfirm")
    public String passwordResetConfirm(Model model,
            @RequestParam("id") long id, @RequestParam("token") String token) {
        UpdatePasswordDto pass = new UpdatePasswordDto();
        String result = securityService.validatePasswordResetToken(id, token);
        if (result != null) {
            model.addAttribute("message", result);
            return "/user/badUser";
        }
        return "redirect:/updatePassword";
    }

    @GetMapping("/updatePassword")
    public String updatePasswordWiew(Model model) {
        UpdatePasswordDto pass = new UpdatePasswordDto();
        model.addAttribute("pass", pass);
        return "/user/updatePassword";

    }

    @PostMapping("/updatePassword")
    public String updatePassword(
            @ModelAttribute("pass") UpdatePasswordDto pass,
            Model model, BindingResult bindingResult) {
        newPasswordValidator.validate(pass, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("status", "Lỗi đầu vào");
            return "/user/updatePassword";
        }
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        userService.changeUserPassword(user, pass.getPassword());
        Authentication auth = null;
        SecurityContextHolder.getContext().setAuthentication(auth);
        model.addAttribute("status", "Đổi mật khẩu thành công");
        return "/user/updatePassword";

    }

    @PostMapping("/user/changePassword")
    @PreAuthorize("hasRole('READ_PRIVILEGE')")
    @ResponseBody
    public String changeUserPassword(@RequestBody NewPassDto pass, BindingResult bindingResult) {
        changePasswordValidator.validate(pass, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/";
        }
        User user = userRepository.findByUserName(
                SecurityContextHolder.getContext().getAuthentication().getName());
        if (!userService.checkIfValidOldPassword(user, pass.getOddPassword())) {
            return "/";
        }
        userService.changeUserPassword(user, pass.getNewPassword());
        return "/";
    }

}
