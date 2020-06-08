/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.scheduled;

import com.ivietech.demo.dao.RechagerRepository;
import com.ivietech.demo.entity.Recharge;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 *
 * @author HoangMinh
 */
public class ScheduledTasks {
    @Autowired
    RechagerRepository rechagerRepository;

    @Scheduled(cron = "0 0 0? * * *")
    public void scheduleTask() {
        List<Recharge> recharges = rechagerRepository.findByStatus("Đang đợi");
        for (Recharge recharge : recharges) {
            recharge.setStatus("Thất bại");
            rechagerRepository.save(recharge);
        }
        

    }
}
