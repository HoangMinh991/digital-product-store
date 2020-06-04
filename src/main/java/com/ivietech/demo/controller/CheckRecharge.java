/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.controller;

import com.ivietech.demo.dao.RechagerRepository;
import com.ivietech.demo.dto.StatusDto;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author HoangMinh
 */
@RestController
public class CheckRecharge {
    @Autowired
    RechagerRepository rechagerRepository;

    @RequestMapping("/user/recharge/info")
    public Object getInfoInvoice(HttpServletRequest request) {
        StatusDto status = new StatusDto();
        if (request.getSession().getAttribute("id") == null) {
            status.setId("0");
            status.setStatus("403");
        } else {
            status.setId((String) request.getSession().getAttribute("id"));
            status.setStatus(rechagerRepository.findById(status.getId()).get().getStatus());
        }
        return status;

    }
}
