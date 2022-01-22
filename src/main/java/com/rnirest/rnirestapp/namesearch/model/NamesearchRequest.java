package com.rnirest.rnirestapp.namesearch.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NamesearchRequest {
    private final String name;
    private final Integer window;
    private final String dob;

    public NamesearchRequest(@JsonProperty("name") String name,
                        @JsonProperty("window") String window,
                        @JsonProperty("dob") String dob) {
        this.name = name;
        this.window = Integer.parseInt(window);
        this.dob = dob;
    }

    @Override
    public String toString() {
        return "[name=" + name + ", window=" + window +"]";
    }

    public String getName(){
        return this.name;
    }

    public Integer getWindow(){
        return this.window;
    }

    public String getDob(){
        return this.dob;
    }
}
