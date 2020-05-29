
package com.ivietech.demo.model.app;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.springframework.stereotype.Component;
public class Action {

    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("trigger_key")
    @Expose
    private String triggerKey;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTriggerKey() {
        return triggerKey;
    }

    public void setTriggerKey(String triggerKey) {
        this.triggerKey = triggerKey;
    }

}
