
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
import com.ivietech.demo.dto.ItemDto;
import com.ivietech.demo.dto.Order;
import com.ivietech.demo.dto.ProductDto;
import com.ivietech.demo.entity.Platforms;
import com.ivietech.demo.entity.Product;
import com.ivietech.demo.entity.Type;
import com.ivietech.demo.entity.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author ADMIN
 */
@Controller
public class CartController {

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

    @GetMapping("/viewCartDetail")
    public String viewCartDetail(Model model) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!"anonymousUser".equals(userName)) {
            User user = userRepository.findByUserName(userName);
            model.addAttribute("user", user);
        }
        List<Platforms> listPlatforms = plaformRepository.findAll();
        List<Type> listType = typeRepository.findAll();
        model.addAttribute("listPlatforms", listPlatforms);
        model.addAttribute("listType", listType);
        return "viewcarddetail";
    }

    @GetMapping("/viewCartDetail/cart/remove")
    public String removeCartOnDetailPage(Model model, @RequestParam(value = "productId", required = false) Integer productId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Order order = (Order) session.getAttribute("order");
        List<ItemDto> items = order.getItems();
        for (ItemDto item : items) {
            if (item.getProductDto().getId() == productId) {
                items.remove(item);
                if (items.isEmpty()) {
                    session.removeAttribute("order");
                    return "redirect:/viewCartDetail";
                }
                break;
            }
        }
        order.setTotal_quantity();
        order.setTotal_order();
        session.setAttribute("order", order);
        return "redirect:/viewCartDetail";
    }

    @GetMapping("/cart/updateGiam")
    public String updateCartGiam(Model model, @RequestParam(value = "productId", required = false) Integer productId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Order order = (Order) session.getAttribute("order");
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ItemDto> items = order.getItems();
        for (ItemDto item : items) {
            if (item.getProductDto().getId() == productId) {
                if (item.getQuantity() == 1) {
                    return "redirect:/viewCartDetail";
                } else {
                    int temp = item.getQuantity();
                    temp -= 1;
                    item.setQuantity(temp);
                    break;
                }
            }
        }
        //Set lại số lượng và total sau khi remove item
        order.setTotal_quantity();
        order.setTotal_order();
        session.setAttribute("order", order);
        return "redirect:/viewCartDetail";
    }

    @GetMapping("/cart/updateTang")
    public String updateCartTang(Model model, @RequestParam(value = "productId", required = false) Integer productId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Order order = (Order) session.getAttribute("order");
        List<ItemDto> items = order.getItems();
        for (ItemDto item : items) {
            if (item.getProductDto().getId() == productId) {
                item.setQuantity(item.getQuantity() + 1);
                break;
            }
        }
        //Set lại số lượng và total sau khi remove item
        order.setTotal_quantity();
        order.setTotal_order();
        session.setAttribute("order", order);
        return "redirect:/viewCartDetail";
    }

    @GetMapping("/cart/remove")
    public String removeCart(Model model, @RequestParam(value = "productId", required = false) Integer productId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Order order = (Order) session.getAttribute("order");
        List<ItemDto> items = order.getItems();
        for (ItemDto item : items) {
            if (item.getProductDto().getId() == productId) {
                items.remove(item);
                if (items.isEmpty()) {
                    System.out.println("Empty");
                    session.removeAttribute("order");
                    return "redirect:/";
                }
            }
        }
        order.setTotal_quantity();
        order.setTotal_order();
        session.setAttribute("order", order);
        return "redirect:/";
    }

    @GetMapping("/cart/buy")
    public String addToCart(Model model, HttpServletRequest request) {
        int quantity = 1;
        long id;
        if (request.getParameter("productId") != null) {
            id = Long.parseLong(request.getParameter("productId"));
            Product product = productRepository.findById(id).get();
            if (product != null) {
                ProductDto productDto = new ProductDto();
                BeanUtils.copyProperties(product, productDto);
                if (request.getParameter("quantity") != null) {
                    quantity = Integer.parseInt(request.getParameter("quantity"));
                }
                HttpSession session = request.getSession();
                if (session.getAttribute("order") == null) {
                    Order order = new Order();
                    List<ItemDto> listItems = new ArrayList<ItemDto>();
                    ItemDto item = new ItemDto();
                    item.setQuantity(quantity);
                    item.setPrice(productDto.getPriceNew() * quantity);
                    item.setProductDto(productDto);
                    listItems.add(item);
                    order.setItems(listItems);
                    order.setTotal_quantity();
                    order.setTotal_order();
                    session.setAttribute("order", order);

                } else {
                    Order order = (Order) session.getAttribute("order");
                    List<ItemDto> listItems = order.getItems();
                    boolean check = false;
                    for (ItemDto item : listItems) {
                        if (item.getProductDto().getId() == productDto.getId()) {
                            item.setQuantity(item.getQuantity() + 1);
                            check = true;
                        }
                    }
                    if (check == false) {
                        ItemDto item = new ItemDto();
                        item.setQuantity(quantity);
                        item.setProductDto(productDto);
                        item.setPrice(quantity * productDto.getPriceNew());
                        listItems.add(item);
                    }
                    order.setTotal_quantity();
                    order.setTotal_order();
                    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
                    session.setAttribute("order", order);
                }
            }
           if( request.getParameter("fast") != null){
               Long fast = Long.parseLong(request.getParameter("fast"));
               if (fast==1) {
                   return "redirect:/viewCartDetail";
               }
           }
            return "redirect:/";
        } else {
            return "error";
        }
    }
}
