/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.event;


import com.ivietech.demo.entity.User;
import org.springframework.context.ApplicationEvent;

/**
 *
 * @author minhh
 */
public class ResetPasswordEvent extends ApplicationEvent {
    private  String appUrl;
    private  User user;
    public ResetPasswordEvent(User user, String appUrl) {
        super(user);
        this.user = user;
        this.appUrl = appUrl;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    
}
