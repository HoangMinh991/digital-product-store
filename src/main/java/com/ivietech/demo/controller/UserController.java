/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.controller;

import com.ivietech.demo.dao.BalanceRepository;
import com.ivietech.demo.dao.OrderRepository;
import com.ivietech.demo.dao.PlaformRepository;
import com.ivietech.demo.dao.ProductRepository;
import com.ivietech.demo.dao.TypeRepository;
import com.ivietech.demo.dao.UserRepository;
import com.ivietech.demo.dto.UpdateUserDto;
import com.ivietech.demo.entity.Platforms;
import com.ivietech.demo.entity.Type;
import com.ivietech.demo.entity.User;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author HoangMinh
 */
@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private BalanceRepository balanceRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private PlaformRepository plaformRepository;

    @GetMapping("/user/info")
    @PreAuthorize("hasRole('READ_PRIVILEGE')")
    public String userInfo(Model model) {
        List<Platforms> listPlatforms = plaformRepository.findAll();
        List<Type> listType = typeRepository.findAll();
        model.addAttribute("listPlatforms", listPlatforms);
        model.addAttribute("listType", listType);   
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUserName(userName);
        model.addAttribute("user", user);
        model.addAttribute("value", user.getBalance().getMoney());
        model.addAttribute("title", "Thông tin tài khoản");
        return "/user/userInfo";
    }
    
    @PostMapping("/user/update")
    @PreAuthorize("hasRole('READ_PRIVILEGE')")
    public String updateUser(Model model, BindingResult result,@Valid UpdateUserDto updateUserDto) {
        if (result.hasErrors()) {
            return "user/userInfo1";
        }
        List<Platforms> listPlatforms = plaformRepository.findAll();
        List<Type> listType = typeRepository.findAll();
        model.addAttribute("listPlatforms", listPlatforms);
        model.addAttribute("listType", listType);
        User user = userRepository.findByUserName(
                SecurityContextHolder.getContext().getAuthentication().getName());
       
        user.setPhone(updateUserDto.getPhone());
        user.setName(updateUserDto.getName());
        model.addAttribute("mesage", "Cập nhật thông tin thành công");
        userRepository.save(user);
        return "/user/userInfo";
    }

}
