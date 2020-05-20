/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.dto;

import com.ivietech.demo.entity.CodeGiftCard;
import com.ivietech.demo.entity.Platforms;
import com.ivietech.demo.entity.ProductDetail;
import com.ivietech.demo.entity.Type;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 *
 * @author ADMIN
 */
@Data
public class ProductDTO implements Serializable {

    private long id;
    private String img;
    private String name;
    private double priceNew;
    private double priceOld;
    private ProductDetail productDetail;
    private Platforms platforms;
    private Type type;
    private boolean best;
    private List<CodeGiftCard> listCodeGiftCard;
    private Date createdDatetime; 

}
