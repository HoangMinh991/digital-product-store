
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.controller;

import com.ivietech.demo.dao.BalanceRepository;
import com.ivietech.demo.dao.CodeGiftCardRepository;
import com.ivietech.demo.dao.OrderRepository;
import com.ivietech.demo.dao.PlaformRepository;
import com.ivietech.demo.dao.ProductRepository;
import com.ivietech.demo.dao.TypeRepository;
import com.ivietech.demo.dao.UserRepository;
import com.ivietech.demo.dto.ItemDto;
import com.ivietech.demo.dto.Order;
import com.ivietech.demo.dto.ProductDto;
import com.ivietech.demo.entity.CodeGiftCard;
import com.ivietech.demo.entity.Platforms;
import com.ivietech.demo.entity.Product;
import com.ivietech.demo.entity.Type;
import com.ivietech.demo.entity.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    @Autowired
    private CodeGiftCardRepository codeGiftCardRepository;

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
                    item.setQuantity(item.getQuantity() - 1);
                    item.setPrice(item.getProductDto().getPriceNew() * item.getQuantity());
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
        List<CodeGiftCard> code = codeGiftCardRepository.getCode(productId, 100);
        System.out.println(code.size());
        HttpSession session = request.getSession();
        Order order = (Order) session.getAttribute("order");
        List<ItemDto> items = order.getItems();
  
        for (ItemDto item : items) {
            if (item.getProductDto().getId() == productId) {
                if (item.getQuantity() == code.size()) {
                    break;
                }
                item.setQuantity(item.getQuantity() + 1);
                item.setPrice(item.getProductDto().getPriceNew() * item.getQuantity());
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
    public String removeCart(@RequestParam(value = "productId", required = false) long productId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Order order = (Order) session.getAttribute("order");
        List<ItemDto> items = new ArrayList<>();
        items = order.getItems();
        System.out.println(items.size());
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getProductDto().getId() == productId) {
                order.getItems().remove(items.get(i));
                if (order.getItems().isEmpty()) {
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
            Optional<ProductDto> productDto = productRepository.findProductDtoById(id);
            List<CodeGiftCard> code = codeGiftCardRepository.getCode((int) id, 100);
            productDto.get().setNumberCode(code.size());
            if (productDto.isPresent()) {
                if (request.getParameter("quantity") != null) {
                    quantity = Integer.parseInt(request.getParameter("quantity"));
                    System.out.println(quantity);
                }
                HttpSession session = request.getSession();
                if (session.getAttribute("order") == null) {
                    Order order = new Order();
                    List<ItemDto> listItems = new ArrayList<ItemDto>();
                    ItemDto item = new ItemDto();
                    item.setQuantity(quantity);
                    item.setPrice(productDto.get().getPriceNew() * item.getQuantity());
                    item.setProductDto(productDto.get());
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
                        if (item.getProductDto().getId() == productDto.get().getId()) {
                            item.setQuantity(item.getQuantity() + quantity);
                            item.setPrice(item.getQuantity() * productDto.get().getPriceNew());
                            check = true;
                            break;
                        }
                    }
                    if (check == false) {
                        ItemDto item = new ItemDto();
                        item.setQuantity(quantity);
                        item.setProductDto(productDto.get());
                        item.setPrice(item.getQuantity() * productDto.get().getPriceNew());
                        listItems.add(item);
                    }
                    order.setTotal_quantity();
                    order.setTotal_order();
                    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
                    session.setAttribute("order", order);
                }
            }
            if (request.getParameter("fast") != null) {
                Long fast = Long.parseLong(request.getParameter("fast"));
                if (fast == 1) {
                    return "redirect:/viewCartDetail";
                }
            }
            if (request.getParameter("quantity") != null) {
                return "redirect:/viewproduct?productId=" + request.getParameter("productId");
            }
            return "redirect:/";
        } else {
            return "error";
        }
    }
}
