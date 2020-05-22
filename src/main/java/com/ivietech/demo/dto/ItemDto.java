/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.dto;

import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author ADMIN
 */
@Data
public class ItemDto implements Serializable{
    private int id;
    private ProductDto productDto;
    private int quantity;
    private double price;
}
