package nl.picnic.interview.test.model;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by hugo on 3/12/16.
 */
public class Autor implements Comparable<Autor> {


    private static final String TWITTER = "EEE MMM dd HH:mm:ss ZZZ yyyy";
    @SerializedName("id")
    private String userId; //The user ID
    @SerializedName("created_at")
    private String creationTime; //The creation date of the user as epoch value
    @SerializedName("name")
    private String name; //The name of the user
    @SerializedName("screen_name")
    private String screenName; //The screen name of the user

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }


    public LocalDateTime getCreationTimeAsALocalDateTime() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TWITTER);

        LocalDateTime localDateTime = LocalDateTime.parse(this.creationTime, formatter);
        return localDateTime;
    }

    @Override
    public boolean equals(Object object) {
        boolean result = false;
        if (object == null || object.getClass() != getClass()) {
            result = false;
        } else {
            Autor autor = (Autor) object;
            result = this.userId.equals(autor.getUserId()) &&
                    this.creationTime.equals(autor.getCreationTime()) &&
                    this.name.equals(autor.getName()) &&
                    this.screenName.equals(autor.getScreenName()) ? true : false;
        }
        return result;
    }

    @Override
    public int compareTo(Autor autor) {
        return this.getCreationTimeAsALocalDateTime().compareTo(autor.getCreationTimeAsALocalDateTime());
    }

    @Override
    public String toString() {
        return "Autor: " + this.name + " [@" + this.screenName + "] - created on: " + this.creationTime + " with id: " + this.userId;
    }
}
