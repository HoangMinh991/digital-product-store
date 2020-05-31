
package com.ivietech.demo.model.sms;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class NotiSms {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("targets")
    @Expose
    private List<String> targets = null;
    @SerializedName("push")
    @Expose
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
