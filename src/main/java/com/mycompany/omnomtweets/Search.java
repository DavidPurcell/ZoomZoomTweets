/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.omnomtweets;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.FilterQuery;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author Purcell7
 */
public class Search {
    /*public static Twitter twitter;
    public static void main(String[] args) throws Exception {
        Candidate candidate = Candidate.TRUMP;
        ConfigurationBuilder cb = new ConfigurationBuilder();
                cb.setDebugEnabled(true)
                  .setOAuthConsumerKey(candidate.consumerKey)
                  .setOAuthConsumerSecret(candidate.consumerSecret)
                  .setOAuthAccessToken(candidate.accessToken)
                  .setOAuthAccessTokenSecret(candidate.accessTokenSecret);
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
    }*/
    public static Candidate candidate = Candidate.CLINTON;
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
    
    public static void main(String[] args) throws TwitterException {
        System.setProperty("twitter4j.loggerFactory", "twitter4j.internal.logging.NullLoggerFactory");
        if(args.length >= 1){
            candidate = Candidate.valueOf(args[0]);
            System.out.println("Getting tweets on " + candidate.name);
        } else {
            //System.out.println("You didn't give me a candidate, so you get TRUMP");
        }
        //System.out.println("Starting at: " + sdf.format(new Date()));
        ConfigurationBuilder cb = new ConfigurationBuilder();
                cb.setDebugEnabled(true)
                  .setOAuthConsumerKey(candidate.consumerKey)
                  .setOAuthConsumerSecret(candidate.consumerSecret)
                  .setOAuthAccessToken(candidate.accessToken)
                  .setOAuthAccessTokenSecret(candidate.accessTokenSecret);

        final ArrayList<Status> statuses = new ArrayList<>();        
        
        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        StatusListener listener = new StatusListener() {

            public void onStatus(Status status) {
                statuses.add(status);
                writeToStdErr(status);
                //Removed because writing to STD OUT instead.
                //System.out.println("statuses: " + statuses.size());
                if(statuses.size() >= 100){
                    /*System.out.println("Writing " + statuses.size() + " to file " + 
                                    "at: " + sdf.format(new Date()));*/
                    writeTweetsToFile(statuses, candidate.name+"StreamTweets.txt");
                    statuses.clear();
                }
            }

            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                //System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                //System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            public void onScrubGeo(long userId, long upToStatusId) {
                //System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            public void onException(Exception ex) {
                //ex.printStackTrace();
            }

            @Override
            public void onStallWarning(StallWarning sw) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };

        FilterQuery fq = new FilterQuery();
        String keywords[] = candidate.aliases;

        fq.track(keywords);

        twitterStream.addListener(listener);
        twitterStream.filter(fq);      
    }
    
    public static boolean writeToStdErr(Status tweet){
        //System.out.println("Writing " + tweets.size() + " tweets");
        boolean success = true;
        if(tweet!= null){
            String tweetText;
            String idOfRetweetee = "";
            if(tweet.getRetweetedStatus()!=null){
                tweetText = "RT " + tweet.getRetweetedStatus().getText();
                idOfRetweetee = "" + tweet.getRetweetedStatus().getUser().getScreenName();
                //System.out.println("retweeted" + tweetText);
            } else {
                tweetText = tweet.getText();
            }
            String urlText = "";
            if(tweet.getURLEntities().length > 0){
                for(URLEntity url:tweet.getURLEntities()){
                    if(url.getExpandedURL() != null){
                        urlText += url.getExpandedURL() + " ";
                        tweetText = tweetText.replace(url.getURL(), url.getExpandedURL());
                        //System.out.println("Expanded URL " + url.getExpandedURL());
                    } else {
                        urlText += url.getURL() + " ";
                        //System.out.println("URL " + url.getURL());
                    }
                }
            }
            if(tweet.getMediaEntities().length > 0){
                for(MediaEntity media:tweet.getMediaEntities()){
                    if(media.getExpandedURL() != null){
                        urlText += media.getExpandedURL() + " ";
                        tweetText = tweetText.replace(media.getMediaURL(), media.getExpandedURL());
                        //System.out.println("Expanded URL " + media.getExpandedURL());
                    } else {
                        urlText += media.getMediaURL() + " ";
                        //System.out.println("URL " + media.getMediaURL());
                    }
                }
            }
            String encodedText = tweetText.replaceAll("\"", "\"\"");
            encodedText = encodedText.replaceAll("\\s"," ");
            String writeMe = "\"" + encodedText + "\"," + urlText + "," +
                    tweet.getUser().getId() + "," + tweet.getId() + ","+ candidate.name +
                    "," + sdf.format(tweet.getCreatedAt()) + "," + idOfRetweetee + "\n";
            System.err.print(writeMe);
            //writeTweets.write(writeMe);
        }
        return success;
    }
    
    /**
     * Method to write the tweets to file, base 64 encoded tweet text.
     * @param tweets the tweets to be written
     * @param filename the file to write the tweets into
     * @return true unless something bad happens
     */
    public static boolean writeTweetsToFile(List<Status> tweets, String filename){
        //System.out.println("Writing " + tweets.size() + " tweets");
        boolean success = true;
        try {
            FileWriter addTweets = new FileWriter(new File(filename), true);
            if(tweets!= null && tweets.size()>0){
                for(Status tweet : tweets){
                    String tweetText;
                    String idOfRetweetee = "";
                    if(tweet.getRetweetedStatus()!=null){
                        tweetText = "RT " + tweet.getRetweetedStatus().getText();
                        idOfRetweetee = "" + tweet.getRetweetedStatus().getUser().getScreenName();
                        //System.out.println("retweeted" + tweetText);
                    } else {
                        tweetText = tweet.getText();
                    }
                    String urlText = "";
                    if(tweet.getURLEntities().length > 0){
                        for(URLEntity url:tweet.getURLEntities()){
                            if(url.getExpandedURL() != null){
                                urlText += url.getExpandedURL() + " ";
                                tweetText = tweetText.replace(url.getURL(), url.getExpandedURL());
                                //System.out.println("Expanded URL " + url.getExpandedURL());
                            } else {
                                urlText += url.getURL() + " ";
                                //System.out.println("URL " + url.getURL());    
                            }
                        }
                    }
                    if(tweet.getMediaEntities().length > 0){
                        for(MediaEntity media:tweet.getMediaEntities()){
                            if(media.getExpandedURL() != null){
                                urlText += media.getExpandedURL() + " ";
                                tweetText = tweetText.replace(media.getMediaURL(), media.getExpandedURL());
                                //System.out.println("Expanded URL " + media.getExpandedURL());
                            } else {
                                urlText += media.getMediaURL() + " ";
                                //System.out.println("URL " + media.getMediaURL());    
                            }
                        }
                    }
                    String encodedText = tweetText.replaceAll("\"", "\"\"");
                    encodedText = encodedText.replaceAll("\n", " ");
                    String writeMe = "\"" + encodedText + "\"," + urlText + "," + 
                            tweet.getUser().getId() + "," + tweet.getId() + ","+ candidate.name + 
                            "," + tweet.getCreatedAt() + "," + idOfRetweetee + "\n";
                    //System.out.println(writeMe);
                    addTweets.write(writeMe);
                }
            }
            addTweets.close();
        } catch (IOException ex) {
            //System.out.println("Something broke lol");
            success = false;
        }
        return success;
    }
}