/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author HoangMinh
 */
@Data
public class UpdateUserDto {

    private String email;
    private String userName;
    @NotNull
    @Size(min = 2, max = 30)
    private String name;
    @NotNull
    private String phone;
}
