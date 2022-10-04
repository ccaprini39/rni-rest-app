package com.rnirest.rnirestapp.namesearch.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AdvancedNamesearchRequest {
    private final String name;
    private final String dob;
    private final Integer window;
    private final Integer threshold;
    private final Integer nameWeight;
    private final Integer dobWeight;

    public AdvancedNamesearchRequest(@JsonProperty("name") String name,
                        @JsonProperty("dob") String dob,
                        @JsonProperty("window") String window,
                        @JsonProperty("threshold") String threshold,
                        @JsonProperty("nameWeight") String nameWeight,
                        @JsonProperty("dobWeight") String dobWeight) {
        this.name = name;
        this.dob = dob;
        this.window = Integer.parseInt(window);
        this.threshold = Integer.parseInt(threshold);
        this.nameWeight = Integer.parseInt(nameWeight);
        this.dobWeight = Integer.parseInt(dobWeight);
    }

    @Override
    public String toString() {
        return "[name=" + name + ", window=" + window +
        ", dob= " + dob + ", threshold=" + Integer.toString(threshold) +
        "]";
    }

    public String getName(){
        return this.name;
    }

    public String getDob(){
        return this.dob;
    }

    public Integer getWindow(){
        return this.window;
    }

    public Integer getThreshold(){
        return this.threshold;
    }

    public Integer getNameWeight(){
        return this.nameWeight;
    }

    public Integer getDobWeight(){
        return this.dobWeight;
    }
}
