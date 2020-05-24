/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author HoangMinh
 */
@Entity
@Data
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codeOrder;
    private String status;
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "order")
    private List<OrderDetails> orderDetails;
    @CreationTimestamp
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date createdDatetime;
    private double total_money;

    
    
}
