package com.ivietech.demo.dto;

import com.ivietech.demo.entity.User;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 *
 * @author PTam
 */
public class Order implements Serializable {

    private User user;
    private List<ItemDto> items;
    private int status;
    private int total_quantity;

    public Order() {
    }
    

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<ItemDto> getItems() {
        return items;
    }

    public void setItems(List<ItemDto> items) {
        this.items = items;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotal_quantity() {
        return total_quantity;
    }

    public void setTotal_quantity(int total_quantity) {
        this.total_quantity = total_quantity;
    }

    public double getTotal_order() {
        return total_order;
    }

    public void setTotal_order(double total_order) {
        this.total_order = total_order;
    }
    private double total_order;

  

    public void setTotal_quantity() {
        int temp = 0;
        for (ItemDto item : items) {
            temp += item.getQuantity();
        }
        this.total_quantity = temp;
    }

    public void setTotal_order() {
        double temp_total = 0;
        for (ItemDto item : items) {
            temp_total += item.getPrice() * item.getQuantity();
        }
        this.total_order = temp_total;
    }


}
