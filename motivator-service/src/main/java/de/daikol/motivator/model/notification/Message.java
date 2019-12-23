package de.daikol.motivator.model.notification;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

/**
 * Created by daikol on 11.12.2016.
 */
@XmlRootElement
public class Message {

    /**
     * The recipient of the message.
     */
    private String to;

    /**
     * Additional data that might be sent.
     */
    private Map<String, String> data;

    /**
     * The notfication itself.
     */
    private Notification notification;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }


}
