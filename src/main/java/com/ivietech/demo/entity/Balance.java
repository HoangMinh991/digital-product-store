/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import lombok.Data;

/**
 *
 * @author HoangMinh
 */
@Entity
@Data
public class Balance implements Serializable{
    @Id
    private long id;
    private long money;
    private long totalMoney;
    @OneToOne
    @PrimaryKeyJoinColumn
    private User user;

}
