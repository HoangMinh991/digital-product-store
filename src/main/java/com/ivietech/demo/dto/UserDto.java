/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.dto;

import com.ivietech.demo.entity.Balance;
import com.ivietech.demo.entity.Orders;
import com.ivietech.demo.entity.Role;
import java.util.Date;
import java.util.List;
import java.util.Set;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 *
 * @author minhh
 */
@Data
public class UserDto {

    private long id;
    private String userName;
    private String name;
    private String password;
    private boolean enabled;
    private String email;
    private String phone;
    private Date createdDatetime;
    private Set<Role> roles;
    Balance balance;
    private List<Orders> listOrder;
}
