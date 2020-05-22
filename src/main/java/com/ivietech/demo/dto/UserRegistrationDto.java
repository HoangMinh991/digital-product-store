/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.dto;

import lombok.Data;

/**
 *
 * @author minhh
 */
@Data
public class UserRegistrationDto {
    private String userName;
    private String name;
    private String password;
    private String passwordConfirm;
    private String email;
    private String phone;   
}
