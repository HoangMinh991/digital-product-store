
package com.ivietech.demo.model.app;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
public class Push {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("source_device_iden")
    @Expose
    private String sourceDeviceIden;
    @SerializedName("source_user_iden")
    @Expose
    private String sourceUserIden;
    @SerializedName("client_version")
    @Expose
    private Integer clientVersion;
    @SerializedName("dismissible")
    @Expose
    private Boolean dismissible;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("application_name")
    @Expose
    private String applicationName;
    @SerializedName("package_name")
    @Expose
    private String packageName;
    @SerializedName("notification_id")
    @Expose
    private String notificationId;
    @SerializedName("actions")
    @Expose
    @Autowired
    @Qualifier("ActionApp")
    private List<Action> actions = null;

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

    public String getSourceUserIden() {
        return sourceUserIden;
    }

    public void setSourceUserIden(String sourceUserIden) {
        this.sourceUserIden = sourceUserIden;
    }

    public Integer getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(Integer clientVersion) {
        this.clientVersion = clientVersion;
    }

    public Boolean getDismissible() {
        return dismissible;
    }

    public void setDismissible(Boolean dismissible) {
        this.dismissible = dismissible;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

}
