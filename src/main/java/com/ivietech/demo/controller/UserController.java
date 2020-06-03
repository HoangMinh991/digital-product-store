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
import com.ivietech.demo.dto.NewPassDto;
import com.ivietech.demo.dto.RechagerDto;
import com.ivietech.demo.dto.UpdateUserDto;
import com.ivietech.demo.entity.Orders;
import com.ivietech.demo.entity.Payment;
import com.ivietech.demo.entity.Platforms;
import com.ivietech.demo.entity.Recharge;
import com.ivietech.demo.entity.Type;
import com.ivietech.demo.entity.User;
import com.ivietech.demo.service.IUserService;
import com.ivietech.demo.validation.ChangePasswordValidator;
import com.ivietech.demo.validation.NewPasswordValidator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author HoangMinh
 */
@Controller
public class UserController {

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
    @Autowired
    private IUserService userService;
    @Autowired
    private NewPasswordValidator newPasswordValidator;
    @Autowired
    private ChangePasswordValidator changePasswordValidator;

    @GetMapping("/user/info")
    @PreAuthorize("hasRole('READ_PRIVILEGE')")
    public String userInfo(Model model) {
        List<Platforms> listPlatforms = plaformRepository.findAll();
        List<Type> listType = typeRepository.findAll();
        model.addAttribute("listPlatforms", listPlatforms);
        model.addAttribute("listType", listType);
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUserName(userName);
        model.addAttribute("user", user);
        model.addAttribute("value", user.getBalance().getMoney());
        model.addAttribute("title", "Thông tin tài khoản");
        return "/user/userInfo";
    }

    @PostMapping("/user/update")
    @PreAuthorize("hasRole('READ_PRIVILEGE')")
    public String updateUser(Model model, BindingResult result, @Valid UpdateUserDto updateUserDto) {
        if (result.hasErrors()) {
            return "user/userInfo1";
        }
        List<Platforms> listPlatforms = plaformRepository.findAll();
        List<Type> listType = typeRepository.findAll();
        model.addAttribute("listPlatforms", listPlatforms);
        model.addAttribute("listType", listType);
        User user = userRepository.findByUserName(
                SecurityContextHolder.getContext().getAuthentication().getName());

        user.setPhone(updateUserDto.getPhone());
        user.setName(updateUserDto.getName());
        model.addAttribute("mesage", "Cập nhật thông tin thành công");
        userRepository.save(user);
        return "/user/userInfo";
    }

    @RequestMapping("/user/order")
    public String viewHistoryOrder(Model model, HttpServletRequest request) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUserName(userName);
        int page = 0; //default page number is 0 (yes it is weird)
        int size = 5; //default page size is 10
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }
        Page<Orders> listOrder = orderRepository.listOrder((int) user.getId(), "OK", PageRequest.of(page, size));
        double total = 0;
        for (Orders order : listOrder) {
            total += order.getTotalMoney();
        }
        model.addAttribute("total", total);
        model.addAttribute("user", user);
        model.addAttribute("orders", listOrder);
        return "user/viewHistory";
    }

    @RequestMapping("/user/searchOrder")
    public String viewSearchOrder(
            @RequestParam(value = "filter_order_id", required = false, defaultValue = "") String order_id,
            @RequestParam(value = "filter_date_added_from", required = false, defaultValue = "1999-1-1") String date_from,
            @RequestParam(value = "filter_total_from", required = false, defaultValue = "0") double total_from,
            @RequestParam(value = "filter_date_added_to", required = false, defaultValue = "2030-1-1") String date_to,
            @RequestParam(value = "filter_total_to", required = false, defaultValue = "999999") double total_to,
            Model model, HttpServletRequest request) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUserName(userName);
        int page = 0; //default page number is 0 (yes it is weird)
        int size = 5; //default page size is 10
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }
        Page<Orders> listOrderSearch = orderRepository.listOrderSearch((int) user.getId(), order_id, "OK", total_from, total_to, date_from, date_to, PageRequest.of(page, size));
        double total = 0;
        for (Orders order : listOrderSearch) {
            total += order.getTotalMoney();
        }
        model.addAttribute("total", total);
        model.addAttribute("user", user);
        model.addAttribute("orders", listOrderSearch);
        return "user/viewHistorySearch";
    }

    @GetMapping("/user/recharge")
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
        List<Payment> payments = paymentRepository.findAll();
        model.addAttribute("payments", payments);
        return "user/listpayment";
    }

    @PostMapping("/user/recharge")
    public String addMoney(Model model, RechagerDto rechagerDto, HttpServletRequest request) {
        Payment payment = paymentRepository.findById(rechagerDto.getIdPayment()).get();
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUserName(userName);
        Recharge recharge = new Recharge();
        recharge.setMoney(rechagerDto.getMoney());
        recharge.setPayment(payment);
        recharge.setStatus("Đang đợi");
        recharge.setUser(user);
        recharge = rechagerRepository.save(recharge);
        request.getSession().setAttribute("id", recharge.getId());
        List<Platforms> listPlatforms = plaformRepository.findAll();
        List<Type> listType = typeRepository.findAll();
        model.addAttribute("recharge", recharge);
        model.addAttribute("user", user);
        model.addAttribute("listPlatforms", listPlatforms);
        model.addAttribute("listType", listType);
        return "user/recharge";
    }

    @GetMapping("user/transaction")
    public String transaction(Model model, HttpServletRequest request) {
        int page = 0; //default page number is 0 (yes it is weird)
        int size = 10; //default page size is 10
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }
        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUserName(userName);
        model.addAttribute("user", user);
        List<Platforms> listPlatforms = plaformRepository.findAll();
        List<Type> listType = typeRepository.findAll();
        model.addAttribute("listPlatforms", listPlatforms);
        Page<Recharge> recharges = rechagerRepository.findByUser(user, PageRequest.of(page, size, Sort.by("id").descending()));
        model.addAttribute("recharges", recharges);
        return "user/viewTransaction";
    }

    @GetMapping("/user/changePassword")
    public String changePassword(Model model) {
        NewPassDto newPassDto = new NewPassDto();
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUserName(userName);
        model.addAttribute("user", user);
        List<Platforms> listPlatforms = plaformRepository.findAll();
        List<Type> listType = typeRepository.findAll();
        model.addAttribute("listPlatforms", listPlatforms);
        model.addAttribute("newPass", newPassDto);
        return "user/userChangePass";
    }

    @PostMapping("/user/changePassword")
    @PreAuthorize("hasRole('READ_PRIVILEGE')")
    public String changeUserPassword(@ModelAttribute("newPass") NewPassDto pass, BindingResult bindingResult, Model model) {
        changePasswordValidator.validate(pass, bindingResult);
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUserName(userName);
        model.addAttribute("user", user);
        List<Platforms> listPlatforms = plaformRepository.findAll();
        List<Type> listType = typeRepository.findAll();
        model.addAttribute("listPlatforms", listPlatforms);
        if (bindingResult.hasErrors()) {
            return "user/userChangePass";
        }
        if (!userService.checkIfValidOldPassword(user, pass.getOddPassword())) {
            model.addAttribute("status", "Mật khẩu cũ không khớp");
            return "user/userChangePass";
        }
        userService.changeUserPassword(user, pass.getNewPassword());
        model.addAttribute("status", "Đổi mật khẩu thành công");
        return "user/userChangePass";
    }

}
