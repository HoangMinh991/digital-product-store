/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.controller;

import com.ivietech.demo.dao.BalanceRepository;
import com.ivietech.demo.dao.OrderRepository;
import com.ivietech.demo.dao.PaymentRepository;
import com.ivietech.demo.dao.PlaformRepository;
import com.ivietech.demo.dao.ProductRepository;
import com.ivietech.demo.dao.RechagerRepository;
import com.ivietech.demo.dao.TypeRepository;
import com.ivietech.demo.dao.UserRepository;
import com.ivietech.demo.dto.RechagerDto;
import com.ivietech.demo.dto.UpdateUserDto;
import com.ivietech.demo.entity.Payment;
import com.ivietech.demo.entity.Platforms;
import com.ivietech.demo.entity.Recharge;
import com.ivietech.demo.entity.Type;
import com.ivietech.demo.entity.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author HoangMinh
 */
@Controller
public class RechangeController {

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
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private RechagerRepository rechagerRepository;

    @GetMapping("/user/payment")
    public String getPayment(Model model) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUserName(userName);
        model.addAttribute("user", user);
        List<Platforms> listPlatforms = plaformRepository.findAll();
        List<Type> listType = typeRepository.findAll();
        model.addAttribute("listPlatforms", listPlatforms);
        RechagerDto rechagerDto = new RechagerDto();
        model.addAttribute("rechagerDto", rechagerDto);
        model.addAttribute("listType", listType);
        return "user/listpayment";
    }
    @PostMapping("/user/recharge")
    public String addMoney(Model model, RechagerDto rechagerDto){
        System.out.println(rechagerDto.getMoney());
        Payment payment = paymentRepository.findById(rechagerDto.getIdPayment()).get();
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUserName(userName);
        Recharge recharge = new Recharge();
        recharge.setMoney(rechagerDto.getMoney());
        recharge.setPayment(payment);
        recharge.setStatus("Đang đợi");
        recharge.setUser(user);
        rechagerRepository.save(recharge);
        
        return "rechange";
    }

}
