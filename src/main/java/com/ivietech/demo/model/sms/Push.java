
package com.ivietech.demo.model.sms;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Push {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("source_device_iden")
    @Expose
    private String sourceDeviceIden;
    @SerializedName("notifications")
    @Expose
    private List<Notification> notifications;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSourceDeviceIden() {
        return sourceDeviceIden;
    }

    public void setSourceDeviceIden(String sourceDeviceIden) {
        this.sourceDeviceIden = sourceDeviceIden;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

}
