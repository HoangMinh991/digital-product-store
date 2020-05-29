/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.dao;

import com.ivietech.demo.entity.User;
import com.ivietech.demo.entity.Balance;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HoangMinh
 */
@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {

    public Optional<Balance> findById(long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE balance SET money = money - :invoice_total WHERE id = :id_user", nativeQuery = true)
    public void changeBalance(@Param("invoice_total") double invoice_total, @Param("id_user") long id_user);

}
