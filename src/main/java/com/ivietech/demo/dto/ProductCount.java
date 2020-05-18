/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.dto;

import com.ivietech.demo.entity.Product;
import lombok.Data;

/**
 *
 * @author HoangMinh
 */
@Data
public class ProductCount {
    private long count;
    private Product product;

    public ProductCount(Product product,long count ) {
        this.count = count;
        this.product = product;
    }
    
}
