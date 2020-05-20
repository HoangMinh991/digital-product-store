package com.ivietech.demo.dto;

import com.ivietech.demo.entity.User;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author PTam
 */
public class Order implements Serializable {

    private int id;
    private UserDto user;
    private List<Item> items;
    private int status;
    private int total_quantity;
    private double total_order;

    public Order() {
    }

    public double getTotal_order() {
        return total_order;
    }

    public int getTotal_quantity() {
        return total_quantity;
    }

    public void setTotal_quantity() {
        int temp = 0;
        for (Item item : items) {
            temp += item.getQuantity();
        }
        this.total_quantity = temp;
    }

    public void setTotal_order() {
        double temp_total = 0;
        for (Item item : items) {
            temp_total += item.getPriceNew() * item.getQuantity();
        }
        this.total_order = temp_total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
