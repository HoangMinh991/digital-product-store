package com.ivietech.demo.dto;

import com.ivietech.demo.entity.User;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 *
 * @author PTam
 */
@Data
public class Order implements Serializable {

    private User user;
    private List<ItemDto> items;
    private int status;
    private int total_quantity;
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
