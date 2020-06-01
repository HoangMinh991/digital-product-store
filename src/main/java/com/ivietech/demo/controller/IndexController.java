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
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.ivietech.demo.dao.UserRepository;
import com.ivietech.demo.dto.ProductDto;
import com.ivietech.demo.entity.Platforms;
import com.ivietech.demo.entity.Type;
import com.ivietech.demo.entity.User;
import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author HoangMinh
 */
@Controller
public class IndexController {

    @Autowired
    private UserRepository accountRepository;
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
   

    @GetMapping("/")
    public String index(Model model, HttpServletRequest request) {
        int page = 0; //default page number is 0 (yes it is weird)
        int size = 4; //default page size is 10
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }
        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
        Page<ProductDto> listSteamProduct = productRepository.findAllByTypeAndPlatforms("Gift card","Steam", PageRequest.of(page, size));
        Page<ProductDto> listGameProduct = productRepository.findAllByType("Game", PageRequest.of(page, size));
        Page<ProductDto> listBestProduct = productRepository.findAllByBestTrue(PageRequest.of(page, size));
        Page<ProductDto> listBestSellProduct = productRepository.findAllByBestSeller(PageRequest.of(page, size));
        Page<ProductDto> listPromotion = productRepository.findAllByPromotion(PageRequest.of(page, size));
     //   Page<ProductDto> listBestSellProduct = productRepository.findAllByBestSeller(PageRequest.of(page, size));
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!"anonymousUser".equals(userName)) {
            User user = accountRepository.findByUserName(userName);
            model.addAttribute("user", user);

        }
        List<Platforms> listPlatforms = plaformRepository.findAll();
        List<Type> listType = typeRepository.findAll();
        model.addAttribute("listPlatforms", listPlatforms);
        model.addAttribute("listType", listType);
        model.addAttribute("listBestProduct", listBestProduct);
        model.addAttribute("listSteamProduct", listSteamProduct);
        model.addAttribute("listGameProduct", listGameProduct);
        model.addAttribute("listBestSellProduct", listBestSellProduct);
        model.addAttribute("listPromotion", listPromotion);
  
        return "index";
    }
}
