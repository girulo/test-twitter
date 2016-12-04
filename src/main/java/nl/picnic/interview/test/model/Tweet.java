package nl.picnic.interview.test.model;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by hugo on 3/12/16.
 */
public class Tweet {

    private static final String TWITTER="EEE MMM dd HH:mm:ss ZZZ yyyy";


    @SerializedName("id")
    private String tweetId; //The message ID
    @SerializedName("created_at")
    private String createdTime; //The creation date of the message as epoch value
    @SerializedName("text")
    private String message; //The text of the message
    @SerializedName("user")
    private Autor autor;// The author of the message


    public String getTweetId() {
        return tweetId;
    }

    public void setTweetId(String tweetId) {
        this.tweetId = tweetId;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public LocalDateTime getCreationTimeAsALocalDateTime() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TWITTER);

        LocalDateTime localDateTime = LocalDateTime.parse(this.createdTime, formatter);
        return  localDateTime;
    }

    @Override
    public String toString() {
        return ("Tweet id: " + this.tweetId + "created on: " + this.createdTime + "\nText: " + this.message);
//        System.out.println("Tweet id: " + this.tweetId + "created on: " + this.createdTime);
//        System.out.println("Text: " + this.message);
//        return null;
    }
}
