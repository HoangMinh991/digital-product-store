
package com.ivietech.demo.model.app;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

public class Noti {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("targets")
    @Expose
    private List<String> targets = null;
    @SerializedName("push")
    @Expose
    @Autowired
    @Qualifier("PushApp")
    private Push push;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getTargets() {
        return targets;
    }

    public void setTargets(List<String> targets) {
        this.targets = targets;
    }

    public Push getPush() {
        return push;
    }

    public void setPush(Push push) {
        this.push = push;
    }

}
