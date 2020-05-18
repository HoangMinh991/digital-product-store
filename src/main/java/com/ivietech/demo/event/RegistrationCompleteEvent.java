package com.ivietech.demo.event;


import com.ivietech.demo.entity.User;
import org.springframework.context.ApplicationEvent;

public class RegistrationCompleteEvent extends ApplicationEvent {

    private String appUrl;
    private User user;


    public RegistrationCompleteEvent(String appUrl, User user, Object source) {
        super(source);
        this.appUrl = appUrl;
        this.user = user;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public User getUser() {
        return user;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }
    

}
