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
public class PageProductDto {
    private ProductDto product;
    private int pageNumber;
    private int totalPage;
}
