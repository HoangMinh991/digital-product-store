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
import java.util.List;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author HoangMinh
 */
@Controller
public class DetailController {

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

    @GetMapping("/detail")
    public String index(Model model, HttpServletRequest request) {
        Page<ProductDto> listProduct = null;
        int page = 0; //default page number is 0 (yes it is weird)
        int size = 8; //default page size is 10
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }
        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
        if (request.getParameter("platform") != null && !request.getParameter("platform").isEmpty()) {
            String platformName = request.getParameter("platform");
            listProduct = productRepository.findAllByPlatforms(platformName, PageRequest.of(page, size));
        }
        if (request.getParameter("type") != null && !request.getParameter("type").isEmpty()) {
            String typeName = request.getParameter("type");
            listProduct = productRepository.findAllByType(typeName, PageRequest.of(page, size));
        }
        if (request.getParameter("type") != null && !request.getParameter("type").isEmpty() && 
                request.getParameter("platform") != null && !request.getParameter("platform").isEmpty() ) {
            String typeName = request.getParameter("type");
            String platformName = request.getParameter("platform");
            listProduct = productRepository.findAllByTypeAndPlatforms(typeName, platformName, PageRequest.of(page, size));
        }
        
        if (request.getParameter("bestseller") != null && !request.getParameter("bestseller").isEmpty()) {
            listProduct = productRepository.findAllByBestSeller(PageRequest.of(page, size));
        }
        if (request.getParameter("promotion") != null && !request.getParameter("promotion").isEmpty()) {
            listProduct = productRepository.findAllByPromotion(PageRequest.of(page, size));
        }
        if (request.getParameter("bestproduct") != null && !request.getParameter("bestproduct").isEmpty()) {
            listProduct = productRepository.findAllByBestTrue(PageRequest.of(page, size));
        }
        model.addAttribute("listProduct", listProduct);
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!"anonymousUser".equals(userName)) {
            User user = accountRepository.findByUserName(userName);
            model.addAttribute("user", user);

        }
        List<Platforms> listPlatforms = plaformRepository.findAll();
        List<Type> listType = typeRepository.findAll();
        model.addAttribute("listPlatforms", listPlatforms);
        model.addAttribute("listType", listType);
        return "detail";
    }

    @GetMapping("/detail/search")
    public String searchProudct(Model model,
            @RequestParam(value = "filter_product_id", required = false, defaultValue = "") String productId,
            @RequestParam(value = "filter_product_name", required = false, defaultValue = "") String productName,
            @RequestParam(value = "filter_type_name", required = false, defaultValue = "") String typeName,
            @RequestParam(value = "filter_plaform_name", required = false, defaultValue = "") String plaformName,
            @RequestParam(value = "filter_price_from", required = false, defaultValue = "0") long priceLow,
            @RequestParam(value = "filter_price_to", required = false, defaultValue = "999999999999999") long priceHigh,
            HttpServletRequest request) {
        int page = 0;
        int size = 8; 
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }
        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
        List<Platforms> listPlatforms = plaformRepository.findAll();
        List<Type> listType = typeRepository.findAll();
        model.addAttribute("listPlatforms", listPlatforms);
        model.addAttribute("listType", listType);
        Page<ProductDto> productDto = productRepository.findAllProductDto(productId, productName, priceLow, priceHigh, typeName, plaformName,  PageRequest.of(page, size));
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!"anonymousUser".equals(userName)) {
            User user = accountRepository.findByUserName(userName);
            model.addAttribute("user", user);
        }
        model.addAttribute("listProduct", productDto);
        return "search";

    }
}
