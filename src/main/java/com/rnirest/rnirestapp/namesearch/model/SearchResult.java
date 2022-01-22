package com.rnirest.rnirestapp.namesearch.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchResult {
    private final String name;
    private final String dob;
    private final String time;
    private final String score;

    public SearchResult(@JsonProperty("name") String name,
                        @JsonProperty("dob") String dob,
                        @JsonProperty("time") String time,
                        @JsonProperty("score") String score) {
        this.name = name;
        this.dob = dob;
        this.time = time;
        this.score = score;
    }

    @Override
    public String toString() {
        return "[name=" + name + ", dob=" + dob + ", time=" + time + "]";
    }

    public String getName(){
        return this.name;
    }

    public String getDob(){
        return this.dob;
    }

    public String getTime(){
        return this.time;
    }

    public String getScore(){
        return this.score;
    }

}
