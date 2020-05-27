/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author HoangMinh
 */
@Entity
@Data
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String img;
    @Column(unique=true)
    private String name;
    private double priceNew;
    private double priceOld;
    @OneToOne
    @PrimaryKeyJoinColumn
    private ProductDetail productDetail;
    @ManyToOne
    private Platforms platforms;
    @ManyToOne(fetch = FetchType.EAGER)
    private Type type;
    private boolean best;
    @OneToMany(mappedBy ="product")
    private List<CodeGiftCard> listCodeGiftCard;
    @OneToMany(mappedBy = "product")
    private List<OrderDetails> listOrderDetail;
    @CreationTimestamp
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date createdDatetime;
}
