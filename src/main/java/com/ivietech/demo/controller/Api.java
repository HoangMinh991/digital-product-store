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
import com.ivietech.demo.entity.Product;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author HoangMinh
 */
@RestController
public class Api {

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

    @GetMapping("api/detail")
    public Page<Product> getProduct(HttpServletRequest request) {
        Page<Product> listProduct = null;
       
        int page = 0; //default page number is 0 (yes it is weird)
        int size = 4; //default page size is 10
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
        if (request.getParameter("bestproduct") != null && !request.getParameter("bestproduct").isEmpty()) {
            listProduct= productRepository.findAllByBestTrue(PageRequest.of(page, size));
        }
        
        return listProduct;
    }

}
