/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.omnomtweets;

/**
 *
 * @author Purcell7
 */
public enum Candidate {
    //CANDIDATE(name, party, account, campainStartDate, consumerKey, consumerSecret, accessToken, accessTokenSecret, aliases)
    //All twitter keys and secrets have been removed and replaced with the name of what should go there.
    TRUMP("Donald Trump","Republican","@realDonaldTrump", "2015-06-16",
            "consumerKey","consumerSecret",
            "accessToken","accessTokenSecret",
            "Donald Trump","@realDonaldTrump","Trump","DonaldTrump"),
    CLINTON("Hillary Clinton","Democray","@HillaryClinton",  "2015-04-12",
            "consumerKey","consumerSecret",
            "accessToken","accessTokenSecret",
            "Hillary Clinton","@HillaryClinton","Clinton","HillaryClinton"),
    /*CARSON("Ben Carson","Republican","@RealBenCarson", "2015-05-03",
            "consumerKey","consumerSecret","accessToken","accessTokenSecret",
            "Ben Carson","@RealBenCarson","Carson","BenCarson"),
    SANDERS("Bernie Sanders","Democrat","@BernieSanders", "2015-04-30",
            "consumerKey","consumerSecret","accessToken","accessTokenSecret",
            "Bernie Sanders","@BernieSanders","Sanders","BernieSanders"),
    CRUZ("Ted Cruz", "Republican","@tedcruz", "2015-01-30",
            "consumerKey","consumerSecret","accessToken","accessTokenSecret",
            "Ted Cruz","@tedcruz","Cruz","TedCruz"*/;
    
    public String[] aliases;
    public String name;
    public String account;
    public String party;
    public String startDate;
    public String consumerKey;
    public String consumerSecret;
    public String accessToken;
    public String accessTokenSecret;
    
    Candidate(String name, String party, String account, String startDate, String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret, String... aliases){
        this.name = name;
        this.party = party;
        this.account = account;
        this.aliases = aliases;
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.accessToken = accessToken;
        this.accessTokenSecret = accessTokenSecret;
        this.startDate = startDate;
    }
}
