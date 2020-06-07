/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.dao;

import com.ivietech.demo.entity.User;
import com.ivietech.demo.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HoangMinh
 */
@Repository
public interface OrderRepository extends JpaRepository<Orders, String> {

    @Query(
            value = "SELECT * FROM orders o WHERE o.user_id = :user_id and o.status = :status",
            nativeQuery = true
    )
    public Page<Orders> listOrder(@Param("user_id") int id, @Param("status") String status, Pageable pageable);

    @Query(
            value = "SELECT * FROM orders o WHERE o.user_id = :user_id and o.id LIKE CONCAT('%',:code_order,'%') and o.total_money >= :total_from "
            + "and o.total_money <= :total_to and o.created_datetime >= :date_from and o.created_datetime < :date_to",
            nativeQuery = true
    )
    public Page<Orders> listOrderSearch(@Param("user_id") long user_id,
            @Param("code_order") String code_order,
            @Param("total_from") long total_from,
            @Param("total_to") long total_to,
            @Param("date_from") String date_from,
            @Param("date_to") String date_to,
            Pageable pageable);

    @Query(
            value = "SELECT * FROM orders o WHERE o.id LIKE CONCAT('%',:code_order,'%') and o.total_money >= :total_from "
            + "and o.total_money <= :total_to and o.created_datetime >= :date_from and o.created_datetime < :date_to",
            nativeQuery = true
    )
    public Page<Orders> listOrderAdminSearch(
            @Param("code_order") String code_order,
            @Param("total_from") long total_from,
            @Param("total_to") long total_to,
            @Param("date_from") String date_from,
            @Param("date_to") String date_to,
            Pageable pageable);

    @Query(
            value = "SELECT * FROM db.orders",
            nativeQuery = true
    )
    public Page<Orders> listOrderAdmin(Pageable pageable);

}
