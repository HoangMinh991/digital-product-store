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
public class ProductDto implements Serializable {

    private long id;
    private String img;
    private String name;
    private double priceNew;
    private double priceOld;
    private long numberCode;
    private long numberOrderCode;
    private String typeName;
    private String platformsName;
    private boolean best;

    public ProductDto(long id, String img, String name, double priceNew, double priceOld, long numberCode) {
        this.id = id;
        this.img = img;
        this.name = name;
        this.priceNew = priceNew;
        this.priceOld = priceOld;
        this.numberCode = numberCode;
    }

    public ProductDto(long id, String img, String name, double priceNew, double priceOld, long numberCode, long numberOrderCode) {
        this.id = id;
        this.img = img;
        this.name = name;
        this.priceNew = priceNew;
        this.priceOld = priceOld;
        this.numberCode = numberCode;
        this.numberOrderCode = numberOrderCode;
    }

    public ProductDto(long id, String img, String name, double priceNew, double priceOld, long numberCode, long numberOrderCode, String typeName, String platformsName, boolean best) {
        this.id = id;
        this.img = img;
        this.name = name;
        this.priceNew = priceNew;
        this.priceOld = priceOld;
        this.numberCode = numberCode;
        this.numberOrderCode = numberOrderCode;
        this.typeName = typeName;
        this.platformsName = platformsName;
        this.best = best;
    }

   
    

    public ProductDto() {
    }

}
