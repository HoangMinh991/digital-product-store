/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.controller;

import com.ivietech.demo.dao.BalanceRepository;
import com.ivietech.demo.dao.OrderRepository;
import com.ivietech.demo.dao.ProductRepository;
import com.ivietech.demo.dao.UserRepository;
import com.ivietech.demo.entity.Product;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author ADMIN
 */
@Controller
public class ProductController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private BalanceRepository balanceRepository;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/viewproduct")
    public String viewproduct(Model model,@RequestParam(value = "productId", required = false) Integer productId) {
        Long temp = new Long(productId);
   //     Product product = productRepository.getProductById(temp);
       // model.addAttribute("product", product);
        return "ProductDetail";
    }
}
