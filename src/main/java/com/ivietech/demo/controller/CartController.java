
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
import com.ivietech.demo.dto.Item;
import com.ivietech.demo.dto.Order;
import com.ivietech.demo.dto.ProductDTO;
import com.ivietech.demo.entity.Platforms;
import com.ivietech.demo.entity.Product;
import com.ivietech.demo.entity.Type;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
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
        List<Platforms> listPlatforms = plaformRepository.findAll();
        List<Type> listType = typeRepository.findAll();
        model.addAttribute("listPlatforms", listPlatforms);
        model.addAttribute("listType", listType);
        return "viewcarddetail";
    }

    @GetMapping("/viewCartDetail/cart/remove")
    public String removeCartOnDetailPage(Model model, @RequestParam(value = "productId", required = false) Integer productId, HttpServletRequest request) {
        System.out.println("Remove" + productId);
        HttpSession session = request.getSession();
        Order order = (Order) session.getAttribute("order");
        List<Item> items = order.getItems();
        for (Item item : items) {
            if (item.getProduct().getId() == productId) {
                items.remove(item);
                if (items.isEmpty()) {
                    System.out.println("Empty");
                    session.removeAttribute("order");
                    return "redirect:/viewCartDetail";
                }
                break;
            }
        }
        //Set lại số lượng và total sau khi remove item
        order.setTotal_quantity();
        order.setTotal_order();
        session.setAttribute("order", order);
        return "redirect:/viewCartDetail";
    }

    @GetMapping("/cart/updateGiam")
    public String updateCartGiam(Model model, @RequestParam(value = "productId", required = false) Integer productId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Order order = (Order) session.getAttribute("order");
        List<Item> items = order.getItems();
        for (Item item : items) {
            if (item.getProduct().getId() == productId) {
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
        List<Item> items = order.getItems();
        for (Item item : items) {
            if (item.getProduct().getId() == productId) {
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
        System.out.println("dddd");
        Order order = (Order) session.getAttribute("order");
        System.out.println(order.getTotal_order());
        System.out.println("abc");
        List<Item> items = order.getItems();
        for (Item item : items) {
            if (item.getProduct().getId() == productId) {
                items.remove(item);
                if (items.isEmpty()) {
                    System.out.println("Empty");
                    session.removeAttribute("order");
                    return "redirect:/";
                }
            }
        }
        //Set lại số lượng và total sau khi remove item
        order.setTotal_quantity();
        order.setTotal_order();
        session.setAttribute("order", order);
        return "redirect:/";
    }

    @GetMapping("/cart/buy")
    public String addToCart(Model model, @RequestParam(value = "productId", required = false) Integer productId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        int quantity = 1;
        long id;
        if (request.getParameter("productId") != null) {
            id = (int) Long.parseLong(request.getParameter("productId"));
            Product product = productRepository.findById(id).get();
       
            if (product != null) {
                ProductDTO product_model = new ProductDTO();
                BeanUtils.copyProperties(product, product_model);
                if (request.getParameter("quantity") != null) {
                    quantity = Integer.parseInt(request.getParameter("quantity"));
                }
                HttpSession session = request.getSession();
                if (session.getAttribute("order") == null) {
                    Order order = new Order();
                    List<Item> listItems = new ArrayList<Item>();
                    Item item = new Item();
                    item.setQuantity(quantity);
                    item.setPriceNew( product_model.getPriceNew());
                    item.setProduct(product_model);
                    item.setId();
                    listItems.add(item);
                    order.setItems(listItems);
                    order.setTotal_quantity();
                    order.setTotal_order();
                    session.setAttribute("order", order);
                    //model.addAttribute("total_hoadon", total_hoadon);
                    //model.addAttribute("total", total);
                } else {
                    Order order = (Order) session.getAttribute("order");
                    List<Item> listItems = order.getItems();
                    boolean check = false;
                    for (Item item : listItems) {
                        if (item.getProduct().getId() == product_model.getId()) {
                            item.setQuantity(item.getQuantity() + 1);
                            check = true;
                        }
                    }
                    if (check == false) {
                        Item item = new Item();
                        item.setQuantity(quantity);
                        item.setProduct(product_model);
                        item.setPriceNew(product_model.getPriceNew());
                        item.setId();
                        listItems.add(item);
                    }
                    order.setTotal_quantity();
                    order.setTotal_order();
                    session.setAttribute("order", order);

                }
               Order order = (Order) session.getAttribute("order");
                System.out.println(order.getTotal_quantity());
            }
            
            return "redirect:/";
        } else {
            return "redirect:/";
        }
    }
}