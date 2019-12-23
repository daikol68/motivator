package de.daikol.motivator.model.notification;

/**
 * Created by daikol on 11.12.2016.
 */
public class Notification {

    /**
     * Indicates notification title.
     */
    private String title;

    /**
     * Indicates notification icon. Sets value to myicon for drawable resource myicon. If you don't send this key in the request, FCM displays the launcher icon specified in your app manifest.
     */
    private String icon;

    /**
     * Indicates notification body text.
     */
    private String body;

    /**
     * Indicates a sound to play when the device receives a notification. Supports default or the filename of a sound resource bundled in the app. Sound files must reside in /res/raw/.
     */
    private String sound;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }
}
