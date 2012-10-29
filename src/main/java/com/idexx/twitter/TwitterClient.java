package com.idexx.twitter;

import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import java.io.IOException;

/**
 * Command line application which reads the Twitter streaming API using the track() method to
 * filter on a set of keywords specified by the first command line parameter.
 * Writes tweets to files specified by the second command line parameter.
 *
 * Make sure that you have a twitter4j.properties file in the same directory or the classpath
 * with your Twitter OAuth credentials.
 */
public class TwitterClient {
    private static String[] filter = new String[] { "alabama", "roll tide"};
    private static String outputPath = "/tmp/tweets";

    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            filter = args[0].split(",");

            if (args.length > 1) {
                outputPath = args[1];
            }
        }

        final TwitterStatusListener listener = new TwitterStatusListener(outputPath);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                listener.close();
            }
        });

        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(listener);

        FilterQuery filterQuery = new FilterQuery().track(filter);
        twitterStream.filter(filterQuery);
    }

}
