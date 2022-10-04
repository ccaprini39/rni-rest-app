package com.rnirest.rnirestapp.namesearch.model;

public class ElasticIndex {

    private String name;

    private ElasticIndex() {}

    public ElasticIndex(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
