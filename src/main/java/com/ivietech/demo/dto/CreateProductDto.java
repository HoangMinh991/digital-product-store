/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.dto;

import lombok.Data;

/**
 *
 * @author HoangMinh
 */
@Data
public class CreateProductDto {
    private long id;
    private String img;
    private String name;
    private long priceNew;
    private long priceOld;
    private long typeId;
    private long platformsId;
    private boolean best;

    public CreateProductDto() {
        this.best = false;
    }
    
}
