/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.model;

import java.io.Serializable;

/**
 *
 * @author ADMIN
 */
public class Item implements Serializable{
    private int id;
    private ProductDTO product;
    private int quantity;
    private long price;

    public Item() {
    }

    public int getId() {
        return id;
    }

    public void setId() {
        int temp = (int) product.getId();
        this.id = temp;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
    
    
}
