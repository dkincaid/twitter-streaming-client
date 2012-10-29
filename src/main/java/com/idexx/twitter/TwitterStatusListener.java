package com.idexx.twitter;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.joda.time.DateTime;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Twitter4J StatusListener that writes statuses to files. By default 10,000 tweets are written into each file.
 * File names will be tweets.xxxxxxx where xxxxxxx is the timestamp (in Unix epoch format) of the created at
 * date of the first tweet in the file.
 */
public class TwitterStatusListener implements StatusListener {
    private final ObjectMapper mapper = new ObjectMapper();
    private FileWriter fileWriter;

    private int tweetCounter = 0;
    private final int TWEETS_PER_FILE = 10000;

    private final String outputPath;

    public TwitterStatusListener(String path) {
        this.outputPath = path;
        mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Override
    public void onStatus(Status status) {
        tweetCounter++;

        Tweet tweet = new Tweet();
        tweet.setId(String.valueOf(status.getId()));
        tweet.setCreatedAt(new DateTime(status.getCreatedAt().getTime()).toString());
        tweet.setText(status.getText());

        if (tweetCounter % 100 == 0) {
            if (tweetCounter % 1000 == 0) {
                System.out.print(" " + tweetCounter + " ");
            } else {
                System.out.print(".");
            }
        }

        try {
            if (tweetCounter == 1 || tweetCounter % TWEETS_PER_FILE == 0) {
                if (fileWriter != null) { fileWriter.close(); }
                fileWriter = new FileWriter(outputPath + "/tweets." + tweet.getCreatedAt().getMillis());
                System.out.print("\n");
            }

            StringWriter sw = new StringWriter();
            mapper.writeValue(sw, tweet);
            fileWriter.write(sw.toString() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
    }

    @Override
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
        System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
    }

    @Override
    public void onScrubGeo(long userId, long upToStatusId) {
        System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
    }

    @Override
    public void onException(Exception ex) {
        ex.printStackTrace();
    }

    public void close() {
        try {
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
