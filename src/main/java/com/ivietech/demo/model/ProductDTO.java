/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.model;

import com.ivietech.demo.entity.CodeGiftCard;
import com.ivietech.demo.entity.Platforms;
import com.ivietech.demo.entity.ProductDetail;
import com.ivietech.demo.entity.Type;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class ProductDTO implements Serializable {

    private long id;
    private String img;
    private String name;
    private double price;
    private ProductDetail productDetail;
    private Platforms platforms;
    private Type type;
    private boolean best;
    private List<CodeGiftCard> listCodeGiftCard;
    private Date createdDatetime;

    public ProductDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ProductDetail getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(ProductDetail productDetail) {
        this.productDetail = productDetail;
    }

    public Platforms getPlatforms() {
        return platforms;
    }

    public void setPlatforms(Platforms platforms) {
        this.platforms = platforms;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isBest() {
        return best;
    }

    public void setBest(boolean best) {
        this.best = best;
    }

    public List<CodeGiftCard> getListCodeGiftCard() {
        return listCodeGiftCard;
    }

    public void setListCodeGiftCard(List<CodeGiftCard> listCodeGiftCard) {
        this.listCodeGiftCard = listCodeGiftCard;
    }

    public Date getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(Date createdDatetime) {
        this.createdDatetime = createdDatetime;
    }
    
    

}
