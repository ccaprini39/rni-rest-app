package com.rnirest.rnirestapp.namesearch.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NamesearchRequest {
    private final String name;
    private final Integer window;


    public NamesearchRequest(@JsonProperty("name") String name,
                        @JsonProperty("dob") Integer window) {
        this.name = name;
        this.window = window;
    }

    @Override
    public String toString() {
        return "[name=" + name + ", window=" + window;
    }

    public String getName(){
        return this.name;
    }

    public Integer getWindow(){
        return this.window;
    }
}
