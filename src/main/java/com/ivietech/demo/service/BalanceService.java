/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.service;


import com.ivietech.demo.dao.BalanceRepository;
import com.ivietech.demo.entity.Balance;
import java.util.Optional;
import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Service;

/**
 *
 * @author HoangMinh
 */
@Service
public class BalanceService {

    @Autowired
    private BalanceRepository balanceRepository;
    @Autowired
    private IUserService userService;

    public Balance addBalance(long id) {
        Balance balance = new Balance();
        balance.setId(id);
        balance.setMoney(0);
        balance.setTotalMoney(0);
        return balanceRepository.save(balance);
    }

    public Balance findBalanceByID(long id) {
        Optional<Balance> findById = balanceRepository.findById(id);
        if (findById != null) {
            return findById.get();
        }
        return null;
    }

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(value = {
        @QueryHint(name = "javax.persistence.lock.timeout", value = "1000")
        , 
    @QueryHint(name = "javax.persistence.lock.scope", value = "EXTENDED")}, forCounting = false)
    public Balance changeMoney(long id, double money) throws Exception {
        Balance balance = findBalanceByID(id);
        balance.setMoney(balance.getMoney() - money);
        if (balance.getMoney() < 0) {
            throw new Exception("No money");
        }

        return balanceRepository.save(balance);
    }

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(value = {
        @QueryHint(name = "javax.persistence.lock.timeout", value = "1000")
        , 
    @QueryHint(name = "javax.persistence.lock.scope", value = "EXTENDED")}, forCounting = false)
    public Balance changeMoney(Balance balance, int money) throws Exception {

        balance.setMoney(balance.getMoney() + money);
       
        if (money > 0) {
            balance.setTotalMoney(balance.getTotalMoney() + money);
        }
        if (balance.getMoney() < 0) {
            throw new Exception("No money");
        }

        return balanceRepository.save(balance);
    }
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(value = {
        @QueryHint(name = "javax.persistence.lock.timeout", value = "1000")
        , 
    @QueryHint(name = "javax.persistence.lock.scope", value = "EXTENDED")}, forCounting = false)
    public Balance changTotalMoney(Balance balance, int money){
        balance.setTotalMoney(balance.getTotalMoney() + money);
        return balanceRepository.save(balance);
    }

}
