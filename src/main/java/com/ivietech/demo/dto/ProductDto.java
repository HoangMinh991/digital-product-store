/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 *
 * @author HoangMinh

*/
@Component
@Data

public class ProductDto {
    private long id;
    private String name;
    private double priceNew;
    private double priceOld;
    private String nameType;
    private String namePlatforms;
    private String img;
}
    

