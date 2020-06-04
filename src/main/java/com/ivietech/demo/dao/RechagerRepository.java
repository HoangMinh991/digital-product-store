/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.dao;

import com.ivietech.demo.entity.Orders;
import com.ivietech.demo.entity.Recharge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ivietech.demo.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author HoangMinh
 */
@Repository
public interface RechagerRepository extends JpaRepository<Recharge, String> {

    public Page<Recharge> findByUser(User user,Pageable of);
    
    @Query(
            value = "SELECT * FROM Recharge r WHERE r.user_id = :user_id and r.id LIKE CONCAT('%',:code_recharge,'%') and r.status LIKE CONCAT('%',:status,'%') and r.money >= :total_from "
            + "and r.money <= :total_to and r.created_datetime >= :date_from and r.created_datetime < :date_to",
            nativeQuery = true
    )
    public Page<Recharge> findRechargeSearchForUser(
            @Param("user_id") long user_id,
            @Param("code_recharge") String code_recharge,
            @Param("status") String status,
            @Param("total_from") long total_from,
            @Param("total_to") long total_to,
            @Param("date_from") String date_from,
            @Param("date_to") String date_to,
            Pageable pageable);
    
    
    @Query(
            value = "SELECT * FROM Recharge r WHERE r.id LIKE CONCAT('%',:code_recharge,'%') and r.status LIKE CONCAT('%',:status,'%') and r.money >= :total_from "
            + "and r.money <= :total_to and r.created_datetime >= :date_from and r.created_datetime <= :date_to",
            nativeQuery = true
    )
    public Page<Recharge> findRechargeSearch(
            @Param("code_recharge") String code_recharge,
            @Param("status") String status,
            @Param("total_from") long total_from,
            @Param("total_to") long total_to,
            @Param("date_from") String date_from,
            @Param("date_to") String date_to,
            Pageable pageable);
}
