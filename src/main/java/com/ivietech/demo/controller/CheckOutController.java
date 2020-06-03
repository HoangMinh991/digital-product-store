/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.controller;

import com.ivietech.demo.dao.BalanceRepository;
import com.ivietech.demo.dao.CodeGiftCardRepository;
import com.ivietech.demo.dao.OrderRepository;
import com.ivietech.demo.dao.OrderrDetailRepository;
import com.ivietech.demo.dao.PlaformRepository;
import com.ivietech.demo.dao.ProductRepository;
import com.ivietech.demo.dao.TypeRepository;
import com.ivietech.demo.dao.UserRepository;
import com.ivietech.demo.dto.ItemDto;
import com.ivietech.demo.dto.Order;
import com.ivietech.demo.entity.CodeGiftCard;
import com.ivietech.demo.entity.OrderDetails;
import com.ivietech.demo.entity.Orders;
import com.ivietech.demo.entity.Product;
import com.ivietech.demo.entity.User;
import com.ivietech.demo.service.BalanceService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUserName(userName);
        String parameter = request.getParameter("order_id");
        if (parameter != null) {
            Optional<Orders> orders = orderRepository.findById(Long.parseLong(parameter));
            Orders order = orders.get();
            model.addAttribute("orders", order);
            model.addAttribute("user", user);
            return "user/viewCheckout";
        }
        //Thuc hien xu ly 
        HttpSession session = request.getSession();
        Order order = (Order) session.getAttribute("order");
        //add to DB orders
        long total_order = order.getTotal_order();
        //Check balance
        if (total_order > user.getBalance().getMoney()) {
            String error_message = "Không đủ tiền vui lòng nạp thêm";
            model.addAttribute("error_message", error_message);
            model.addAttribute("user", user);
            return "viewcarddetail";
        }
        List<ItemDto> items = order.getItems();
        for (ItemDto item : items) {
            long id = item.getProductDto().getId();
            int quantity = item.getQuantity();
            List<CodeGiftCard> code = new ArrayList<>();
            code = codeGiftCardRepository.getCode((int) id, quantity);
            if (code.size() < quantity) {
                String error_message = "Không đủ sản phẩm, vui lòng xem lại đơn hàng";
                model.addAttribute("error_message", error_message);
                model.addAttribute("user", user);
                return "viewcarddetail";
            }
        }
        Orders orders = new Orders();
        orders.setUser(user);
        orders.setTotalMoney(total_order);
        orders = orderRepository.save(orders);
        List<OrderDetails> orderDetailses = new ArrayList<>();
        //orders = orderRepository.save(orders);

        for (ItemDto item : items) {
            long id = item.getProductDto().getId();
            long quantity = item.getQuantity();
            List<CodeGiftCard> code = new ArrayList<>();
            code = codeGiftCardRepository.getCode(id, quantity);
            Optional<Product> findById = productRepository.findById(id);
            System.out.println("abcd");
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setOrder(orders);
            orderDetails.setProduct(findById.get());
            System.out.println("abcd1");
            orderDetails.setQuantity(quantity);
            orderDetails.setListCodeGiftCard(code);
            OrderDetails save = orderrDetailRepository.save(orderDetails);
            System.out.println("abcd2");
            save.setListCodeGiftCard(code);
            orderrDetailRepository.save(save);
             System.out.println("abcd3");
            //orderrDetailRepository.save(orderDetails);
            orderDetailses.add(orderDetails);
            for (CodeGiftCard codeGiftCard : code) {
                System.out.println(codeGiftCard.getCode());
                codeGiftCard.setOrderDetails(orderDetails);
            }
             System.out.println("abcd5");

        }
        //Thay doi vi tien cua user
        balanceService.changeMoney(user.getId(), total_order);
        model.addAttribute("user", user);
        //Xoa session sau khi tru tien
        session.removeAttribute("order");
        orders = orderRepository.findById(orders.getId()).get();
        orders.setStatus("OK");
        orders.setOrderDetails(orderDetailses);
        orders = orderRepository.save(orders);
        model.addAttribute("orders", orders);
        return "user/viewCheckout";
    }
}
