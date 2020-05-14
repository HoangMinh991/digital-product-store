/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.dao;



import com.ivietech.demo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author HoangMinh
 */
public interface RoLeRepository extends JpaRepository<Role, Long>{
    Role findByName(String name);
    
    @Override
    void delete(Role role);

    public Role findById(long i);
}
