package nl.picnic.interview.test;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.gson.Gson;
import nl.picnic.interview.test.model.Autor;
import nl.picnic.interview.test.model.Tweet;
import org.interview.oauth.twitter.TwitterAuthenticationException;
import org.interview.oauth.twitter.TwitterAuthenticator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by hugo on 3/12/16.
 */
public class Main {

//    private static final String TRACK = "bieber"; // String to search for tweets in Twitter
//    private static final Integer MAX_WAITING_TIME = 30; //Max time we will wait for tweets, in secs
//    private static final Integer MAX_MESSAGES = 100; //Max number of messages we want to get
//
//    private static final String TWITTER_URL_API = "https://stream.twitter.com/1.1/statuses/filter.json";

    private static final Properties prop = getProperties();

    public static void main(String[] args) throws TwitterAuthenticationException {

//        Properties prop = getProperties();
        TwitterAuthenticator twitterAuthenticator = new TwitterAuthenticator(System.out, prop.getProperty("consumerKey"), prop.getProperty("consumerSecret"));

        HttpRequestFactory factory = twitterAuthenticator.getAuthorizedHttpRequestFactory();

        getTweetsFromTwitterWith(factory);

    }

    private static void getTweetsFromTwitterWith(HttpRequestFactory factory) {

        GenericUrl url = new GenericUrl(createCustomUrlToQuery(prop.getProperty("TWITTER_URL_API")));
        Gson gson = new Gson();
        try {
            HttpResponse tweeterResponse = factory.buildGetRequest(url).execute();
            tweeterResponse.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(tweeterResponse.getContent()));

            TreeMap<Autor, List<Tweet>> tweetsMap = new TreeMap<Autor, List<Tweet>>();

            LocalDateTime startCaptureTime = LocalDateTime.now();
            Integer tweets = 0;
            while (Duration.between(startCaptureTime, LocalDateTime.now()).getSeconds() < Integer.valueOf(prop.getProperty("MAX_WAITING_TIME")) &&
                    tweets < Integer.valueOf(prop.getProperty("MAX_MESSAGES"))) {
                Tweet tweet = gson.fromJson(reader.readLine(), Tweet.class);
                addTweetToMap(tweetsMap, tweet);
                tweets++;
            }

            printResult(tweetsMap);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void printResult(TreeMap<Autor, List<Tweet>> tweetsMap) {

        tweetsMap.forEach((autor, tweetList) -> {
            System.out.println("====================================================================");
            System.out.println(autor.toString());
            System.out.println("Number of capture tweets: " + tweetList.size());
            tweetList.forEach(tweet -> {
                System.out.println(tweet.toString());
            });
            System.out.println("====================================================================");
            System.out.println("\n\n");

        });

    }

    private static void addTweetToMap(TreeMap<Autor, List<Tweet>> tweetsMap, Tweet tweet) {
        Autor autor = tweet.getAutor();
        if (tweetsMap.containsKey(autor)) {
            List<Tweet> tweets = new ArrayList<>();
            tweets.addAll(tweetsMap.get(autor));
            tweets.add(tweet);
            tweets.sort((t1, t2) -> t1.getCreationTimeAsALocalDateTime().compareTo(t2.getCreationTimeAsALocalDateTime()));
            tweetsMap.replace(autor, tweets);
        } else {
            tweetsMap.put(autor, Arrays.asList(tweet));
        }
    }

    private static String createCustomUrlToQuery(String track) {
        return prop.getProperty("TWITTER_URL_API") + "?track=" + prop.getProperty("TRACK");
    }

    public static Properties getProperties() {

        Properties prop = new Properties();
        String propertiesFile = "app.properties";
        try {
            InputStream input = Main.class.getClassLoader().getResourceAsStream(propertiesFile);
            if (input == null) {
                System.out.println("Sorry, unable to find " + propertiesFile);
                return null;
            }

            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }

}
