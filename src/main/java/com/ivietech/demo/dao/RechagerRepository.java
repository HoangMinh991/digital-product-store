/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.dao;

import com.ivietech.demo.entity.Recharge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ivietech.demo.entity.User;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author HoangMinh
 */
@Repository
public interface RechagerRepository extends JpaRepository<Recharge, Long> {

    public Page<Recharge> findByUser(User user,Pageable of);
    
}
