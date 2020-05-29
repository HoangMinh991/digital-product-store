/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author HoangMinh
 */
@Entity
@Data
<<<<<<< HEAD
public class CodeGiftCard {
=======
public class CodeGiftCard{
>>>>>>> ffce9387604eae47d0cf6ea538223b0c66c840ba

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    @CreationTimestamp
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date createdDatetime;
    @ManyToOne
    private Product product;
    @ManyToOne
<<<<<<< HEAD
    private OrderDetails orderDetails;
    private boolean enabled;
=======
    private OrderDetails orderDetails;   

>>>>>>> ffce9387604eae47d0cf6ea538223b0c66c840ba
}
