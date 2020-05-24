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
import com.ivietech.demo.dto.Order;
import com.ivietech.demo.dto.UserDto;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.ivietech.demo.dao.CodeGiftCardRepository;
import com.ivietech.demo.dao.OrderrDetailRepository;
import com.ivietech.demo.dto.Item;
import com.ivietech.demo.dto.ProductDto;
import com.ivietech.demo.entity.CodeGiftCard;
import com.ivietech.demo.entity.OrderDetails;
import com.ivietech.demo.entity.Orders;
import com.ivietech.demo.entity.Product;
import com.ivietech.demo.entity.User;
import com.ivietech.demo.service.BalanceService;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author ADMIN
 */
@Controller
public class CheckOutController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderrDetailRepository orderrDetailRepository;
    @Autowired
    private BalanceRepository balanceRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private PlaformRepository plaformRepository;
    @Autowired
    private CodeGiftCardRepository codeGiftCardRepository;
    @Autowired
    private BalanceService balanceService;

    @GetMapping("/checkout")
    public String test(Model model, HttpServletRequest request) throws Exception {
        //Thuc hien xu ly 
        HttpSession session = request.getSession();
        Order order = (Order) session.getAttribute("order");
        //add to DB orders
        double total_order = order.getTotal_order();
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUserName(userName);
        balanceService.changeMoney(user.getId(), total_order);
        model.addAttribute("user", user);
        Orders orders = new Orders();
        orders.setStatus("OK");
        orders.setUser(user);
        orders.setTotal_money(total_order);
        orderRepository.save(orders);
        
        List<Item> items = order.getItems();
        for (Item item : items) {
            int id = item.getId();
            int quantity = item.getQuantity();
            List<CodeGiftCard> code = codeGiftCardRepository.getCode(id, quantity);
            ProductDto product = item.getProduct();
            product.setListCodeGiftCard(code);
            item.setProduct(product);
            for (CodeGiftCard codeGiftCard : code) {
                System.out.println(codeGiftCard.getCode());
                codeGiftCardRepository.updateBlockGiftCode(codeGiftCard.getCode());
            }
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setOrder(orders);
            Optional<Product> findById = productRepository.findById(product.getId());
            orderDetails.setProduct(findById.get());
            orderDetails.setListCodeGiftCard(code);
            orderDetails.setQuanity(quantity);
            orderrDetailRepository.save(orderDetails);
        }
        //Lay total_invoice + iduser
        //Thay doi vi tien cua user
        session.removeAttribute("order");
        model.addAttribute("user", user);
        return "user/viewCheckout";

    }
}
